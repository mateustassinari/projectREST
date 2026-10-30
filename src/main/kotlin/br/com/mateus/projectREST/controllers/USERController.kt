package br.com.mateus.projectREST.controllers

import br.com.mateus.projectREST.dtos.*
import br.com.mateus.projectREST.entities.USEREntity
import br.com.mateus.projectREST.response.Response
import br.com.mateus.projectREST.security.UserSS
import br.com.mateus.projectREST.services.USERService
import br.com.mateus.projectREST.services.impl.UserService
import br.com.mateus.projectREST.utils.CPFUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.*


@RestController
@RequestMapping("/user")
class USERController(val userService: USERService) {

    @Operation(summary = "Return User by login")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successful request"),
        ApiResponse(responseCode = "401", description = "Not authorized"),
        ApiResponse(responseCode = "404", description = "Not found"),
        ApiResponse(responseCode = "400", description = "Parameter not informed"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(method = [RequestMethod.GET])
    fun findUser(@RequestParam(value="login") login: String?): ResponseEntity<USERDto> {
        val response: USERDto?

        if (login == null) {
            return ResponseEntity.badRequest().build()
        }

        val obj: Optional<USEREntity> = userService.findUserByLogin(login)
        if (obj.isEmpty) {
            return ResponseEntity.notFound().build()
        } else {
            val user: UserSS? = UserService().authenticated()
            if (user != null) {
                if (login != user.login) {
                    throw AuthorizationServiceException("Access denied!")
                }
            }
        }

        response = userService.convertUSERDto(obj.orElse(null))
        return ResponseEntity.ok(response!!)
    }

    @Operation(summary = "Return User by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successful request"),
        ApiResponse(responseCode = "401", description = "Not authorized"),
        ApiResponse(responseCode = "404", description = "Not found"),
        ApiResponse(responseCode = "400", description = "Parameter not informed"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun findUserId(@PathVariable id: Int?): ResponseEntity<USERDto> {
        val response: USERDto?

        val obj: Optional<USEREntity> = userService.findUserById(id!!)
        if(obj.isEmpty) {
            return ResponseEntity.notFound().build()
        }

        response = userService.convertUSERDto(obj.orElse(null))
        return ResponseEntity.ok(response!!)
    }

    @Operation(summary = "Register a new User")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "User Created - New resource URI in header"),
        ApiResponse(responseCode = "409", description = "User already registered"),
        ApiResponse(responseCode = "400", description = "Lack of information/poorly formatted request"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(method = [RequestMethod.POST])
    fun addUser(@Valid @RequestBody userDto: USERInsDto): ResponseEntity<Response<ReturnUSERDto>> {
        val response: Response<ReturnUSERDto> = Response()

        var userExists: Optional<USEREntity> = userService.findUserByLogin(userDto.login!!)
        if(!userExists.isEmpty) {
            response.erros.add("Login already registered!")
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
        }
        userExists = userService.findUserByCPF(userDto.cpf!!)
        if(!userExists.isEmpty) {
            response.erros.add("CPF already registered!")
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
        }
        if(userDto.mail != null) {
            userExists = userService.findUserByMail(userDto.mail!!)
            if (!userExists.isEmpty) {
                response.erros.add("Mail already registered!")
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
            }
        }

        if(!CPFUtil.myValidateCPF(userDto.cpf!!)) {
            response.erros.add("CPF invalid!")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        } else if(!userDto.telephone!!.matches("^[0-9]*$".toRegex())) {
            response.erros.add("Telephone does not contain only numbers!")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        } else if(!userDto.mail!!.matches("^([\\w.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)\$".toRegex())) {
            response.erros.add("Mail is not valid!")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        val obj: NewUSERDto = userService.convertDtoToNewUSER(userDto)
        obj.user = userService.persist(obj.user!!)

        val uri: URI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.user!!.idUser).toUri()

        response.data = ReturnUSERDto(obj.user!!.idUser,obj.newPass)
        return ResponseEntity.created(uri).body(response)
    }

    @Operation(summary = "Update a User")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Updated User"),
        ApiResponse(responseCode = "401", description = "Not authorized"),
        ApiResponse(responseCode = "400", description = "Lack of information/poorly formatted request"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.PUT])
    fun updateUser(@Valid @RequestBody userDto: USERUpdDto, @PathVariable id: Int?): ResponseEntity<Response<Void>> {
        val response: Response<Void> = Response()

        if(!userDto.telephone!!.matches("^[0-9]*$".toRegex())) {
            response.erros.add("Telephone does not contain only numbers!")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        val obj: USEREntity = userService.convertDtoToUSER(userDto)
        obj.idUser = id
        userService.update(obj)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Delete a User")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted User"),
        ApiResponse(responseCode = "401", description = "Not authorized"),
        ApiResponse(responseCode = "400", description = "Lack of information/poorly formatted request"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.DELETE])
    fun delUser(@PathVariable id: Int?): ResponseEntity<Response<Void>> {

        val userExists = userService.findUserById(id!!)
        if(userExists.isEmpty) {
            return ResponseEntity.notFound().build()
        }

        userService.delete(id)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Get address through zip code")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successful request"),
        ApiResponse(responseCode = "400", description = "Lack of information or invalid zip code"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(value = ["/cep/{cep}"],method = [RequestMethod.GET])
    fun verifyCEP(@PathVariable cep: String?): ResponseEntity<CEPDto>? {
        val restTemplate = RestTemplate()
        val url = "http://viacep.com.br/ws/$cep/json/"
        val end: CEPDto? = restTemplate
            .getForObject(url, CEPDto::class.java)

        return end?.let { ResponseEntity.ok(it) }
    }
}
