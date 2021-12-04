package br.com.mateus.projetoREST.controllers

import br.com.mateus.projetoREST.dtos.LoginDto
import br.com.mateus.projetoREST.dtos.PasswDto
import br.com.mateus.projetoREST.dtos.UserLoginDto
import br.com.mateus.projetoREST.dtos.ValidTokenDto
import br.com.mateus.projetoREST.security.JWTUtil
import br.com.mateus.projetoREST.security.UserSS
import br.com.mateus.projetoREST.services.impl.AuthService
import br.com.mateus.projetoREST.services.impl.UserService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/auth")
class AuthController(val jwtUtil : JWTUtil, val authService: AuthService) {

    @ApiOperation("Refresh on authentication token before expiring")
    @ApiResponses(value = [ApiResponse(code = 204, message = "Updated token"),
        ApiResponse(code = 401, message = "Not authorized"),
        ApiResponse(code = 403, message = "Inactive User"),
        ApiResponse(code = 500, message = "Unexpected error")] )
    @RequestMapping(value = ["/refresh_token"], method = [RequestMethod.POST])
    fun refreshToken(): ResponseEntity<ValidTokenDto>? {
        val response: ValidTokenDto?
        val user: UserSS? = UserService().authenticated()
        val token: String = jwtUtil.generateToken(user!!.username)

        return if(authService.validLogin(user.login)) {
            response = authService.convertValidTokenDto(token)
            ResponseEntity.status(HttpStatus.OK).body(response)
        }else{

            ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }
    }

    @ApiOperation("Save user password")
    @ApiResponses(value = [ApiResponse(code = 204, message = "Password changed"),
        ApiResponse(code = 404, message = "No user found for the login provided"),
        ApiResponse(code = 400, message = "Lack of information"),
        ApiResponse(code = 500, message = "Unexpected error"),
        ApiResponse(code = 401, message = "Not authorized")] )
    @RequestMapping(value = ["/password"], method = [RequestMethod.POST])
    fun password(@Valid @RequestBody objDto: PasswDto): ResponseEntity<Void?>? {
        val ok = authService.refreshPassword(objDto.password,objDto.login)
        return if(ok) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @ApiOperation("Login")
    @PostMapping("/login")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Login successfully"),
        ApiResponse(code = 403, message = "Incorrect Data/Authentication Failure"),
        ApiResponse(code = 400, message = "Lack of information"),
        ApiResponse(code = 500, message = "Unexpected error")] )
    fun fakeLogin(@Valid @RequestBody objDto: LoginDto): ResponseEntity<UserLoginDto> {
        throw IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.")
    }


}