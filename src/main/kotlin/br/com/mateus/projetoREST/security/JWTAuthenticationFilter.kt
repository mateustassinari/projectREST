package br.com.mateus.projetoREST.security

import br.com.mateus.projetoREST.entities.Credentials
import br.com.mateus.projetoREST.dtos.UserLoginDto
import br.com.mateus.projetoREST.response.Response
import br.com.mateus.projetoREST.services.USERService
import br.com.mateus.projetoREST.entities.USEREntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTAuthenticationFilter: UsernamePasswordAuthenticationFilter {

    private var jwtUtil: JWTUtil

    @Autowired
    private  var user: USERService

    constructor(authenticationManager: AuthenticationManager, jwtUtil: JWTUtil, user: USERService) : super() {
        this.authenticationManager = authenticationManager
        this.jwtUtil = jwtUtil
        this.user = user
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {
            val (login, password) = ObjectMapper().readValue(request.inputStream, Credentials::class.java)
            try {
                val token = UsernamePasswordAuthenticationToken(login, password)
                return authenticationManager.authenticate(token)
            } catch (e: Exception) {
                throw UsernameNotFoundException("Incorrect Data!")
            }
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse, chain: FilterChain?, authResult: Authentication) {
        val login = (authResult.principal as UserSS).login
        val responsedto: Response<UserLoginDto> = Response()
        val obj: Optional<USEREntity> = user.findUserByLogin(login!!)
        val token = jwtUtil.generateToken(login)
        if(!obj.isEmpty) {
            if(obj.orElse(null).status != "A"){
                response.addHeader("Content-Type","application/json; charset=utf-8")
                response.status = HttpServletResponse.SC_FORBIDDEN
                response.writer?.write("{ \"erros\": [ \"This user is inactive!\" ] }")
                response.writer?.flush()
                response.writer?.close()
                return
            }else {
                responsedto.data = converterUsuarioDto(obj.orElse(null), token)
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

    override fun unsuccessfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, failed: AuthenticationException?) {
        val responsedto: Response<Response<Void>> = Response()
        responsedto.erros.add(failed?.message!!)
        val gson = Gson()
        val json: String = gson.toJson(responsedto)
        response?.addHeader("Content-Type","application/json; charset=utf-8")
        response?.status = HttpServletResponse.SC_NOT_FOUND
        response?.writer?.write(json)
        response?.writer?.flush()
        response?.writer?.close()
    }

}
