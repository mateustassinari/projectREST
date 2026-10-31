package br.com.mateus.projectREST.security

import br.com.mateus.projectREST.entities.Credentials
import br.com.mateus.projectREST.dtos.UserLoginDto
import br.com.mateus.projectREST.response.Response
import br.com.mateus.projectREST.services.USERService
import br.com.mateus.projectREST.entities.USEREntity
import tools.jackson.module.kotlin.jacksonObjectMapper
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import kotlin.jvm.optionals.getOrNull
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse


class JWTAuthenticationFilter: UsernamePasswordAuthenticationFilter {

    private var jwtUtil: JWTUtil

    @Autowired
    private  var user: USERService

    constructor(authenticationManager: AuthenticationManager, jwtUtil: JWTUtil, user: USERService) : super() {
        this.authenticationManager = authenticationManager
        this.jwtUtil = jwtUtil
        this.user = user
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
            val (login, password) = jacksonObjectMapper().readValue(request.inputStream, Credentials::class.java)
            try {
                val token = UsernamePasswordAuthenticationToken(login, password)
                return authenticationManager.authenticate(token)
            } catch (e: Exception) {
                throw UsernameNotFoundException("Incorrect Data!")
            }
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val login = (authResult.principal as UserSS).login
        val responsedto: Response<UserLoginDto> = Response()
        val obj: USEREntity? = user.findUserByLogin(login!!).getOrNull()
        val token = jwtUtil.generateToken(login)
        if(obj != null) {
            if(obj.status != "A"){
                response.addHeader("Content-Type","application/json; charset=utf-8")
                response.status = HttpServletResponse.SC_FORBIDDEN
                response.writer?.write("{ \"errors\": [ \"This user is inactive!\" ] }")
                response.writer?.flush()
                response.writer?.close()
                return
            }else {
                responsedto.data = converterUsuarioDto(obj, token)
            }
        }
        val gson = Gson()
        val json: String = gson.toJson(responsedto.data)
        response.addHeader("Content-Type","application/json; charset=utf-8")
        response.writer.write(json)
        response.writer.flush()
        response.writer.close()
    }

    private fun converterUsuarioDto(obj: USEREntity?, token: String): UserLoginDto? {
        if(obj != null) {
           return UserLoginDto(obj.idUser,obj.nameUser,obj.login, obj.status,token,obj.mail,obj.telephone,obj.cpf)
        }
        return null
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        val responsedto: Response<Response<Void>> = Response()
        responsedto.errors.add(failed.message!!)
        val gson = Gson()
        val json: String = gson.toJson(responsedto)
        response.addHeader("Content-Type","application/json; charset=utf-8")
        response.status = HttpServletResponse.SC_NOT_FOUND
        response.writer?.write(json)
        response.writer?.flush()
        response.writer?.close()
    }

}
