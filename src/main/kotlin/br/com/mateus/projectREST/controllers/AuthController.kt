package br.com.mateus.projectREST.controllers

import br.com.mateus.projectREST.dtos.LoginDto
import br.com.mateus.projectREST.dtos.PasswDto
import br.com.mateus.projectREST.dtos.UserLoginDto
import br.com.mateus.projectREST.dtos.ValidTokenDto
import br.com.mateus.projectREST.services.impl.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class AuthController(val authService: AuthService) {

    @Operation(summary = "Refresh on authentication token before expiring")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Updated token"),
        ApiResponse(responseCode = "401", description = "Not authorized"),
        ApiResponse(responseCode = "403", description = "Inactive User"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(value = ["/refresh_token"], method = [RequestMethod.POST])
    fun refreshToken(): ResponseEntity<ValidTokenDto> {
        val response = authService.refreshToken() ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Save user password")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Password changed"),
        ApiResponse(responseCode = "404", description = "No user found for the login provided"),
        ApiResponse(responseCode = "400", description = "Lack of information"),
        ApiResponse(responseCode = "500", description = "Unexpected error"),
        ApiResponse(responseCode = "401", description = "Not authorized")])
    @RequestMapping(value = ["/password"], method = [RequestMethod.POST])
    fun password(@Valid @RequestBody objDto: PasswDto): ResponseEntity<Void> {
        val ok = authService.refreshPassword(objDto.password,objDto.login)
        return if(ok) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Login successfully"),
        ApiResponse(responseCode = "403", description = "Incorrect Data/Authentication Failure"),
        ApiResponse(responseCode = "400", description = "Lack of information"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    fun fakeLogin(@Valid @RequestBody objDto: LoginDto): ResponseEntity<UserLoginDto> {
        throw IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.")
    }
}
