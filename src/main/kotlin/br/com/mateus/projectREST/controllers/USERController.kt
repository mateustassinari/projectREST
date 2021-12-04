package br.com.mateus.projectREST.controllers

import br.com.mateus.projectREST.dtos.*
import br.com.mateus.projectREST.entities.USEREntity
import br.com.mateus.projectREST.response.Response
import br.com.mateus.projectREST.security.UserSS
import br.com.mateus.projectREST.services.USERService
import br.com.mateus.projectREST.services.impl.UserService
import br.com.mateus.projectREST.utils.CPFUtil
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/user")
class USERController(val userService: USERService) {

    @ApiOperation("Return User by login")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Successful request"),
        ApiResponse(code = 401, message = "Not authorized"),
        ApiResponse(code = 404, message = "Not found"),
        ApiResponse(code = 400, message = "Parameter not informed"),
        ApiResponse(code = 500, message = "Unexpected error")] )
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

    @ApiOperation("Return User by id")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Successful request"),
        ApiResponse(code = 401, message = "Not authorized"),
        ApiResponse(code = 404, message = "Not found"),
        ApiResponse(code = 400, message = "Parameter not informed"),
        ApiResponse(code = 500, message = "Unexpected error")] )
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

    @ApiOperation("Register a new User")
    @ApiResponses(value = [ApiResponse(code = 201, message = "User Created - New resource URI in header"),
        ApiResponse(code = 409, message = "User already registered"),
        ApiResponse(code = 400, message = "Lack of information/poorly formatted request"),
        ApiResponse(code = 500, message = "Unexpected error")] )
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

    @ApiOperation("Update a User")
    @ApiResponses(value = [ApiResponse(code = 204, message = "Updated User"),
        ApiResponse(code = 401, message = "Not authorized"),
        ApiResponse(code = 400, message = "Lack of information/poorly formatted request"),
        ApiResponse(code = 500, message = "Unexpected error")] )
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

    @ApiOperation("Delete a User")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Deleted User"),
        ApiResponse(code = 401, message = "Not authorized"),
        ApiResponse(code = 400, message = "Lack of information/poorly formatted request"),
        ApiResponse(code = 500, message = "Unexpected error")] )
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.DELETE])
    fun delUser(@PathVariable id: Int?): ResponseEntity<Response<Void>> {

        val userExists = userService.findUserById(id!!)
        if(userExists.isEmpty) {
            return ResponseEntity.notFound().build()
        }

        userService.delete(id)
        return ResponseEntity.ok().build()
    }

    @ApiOperation("Get address through zip code")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Successful request"),
        ApiResponse(code = 400, message = "Lack of information or invalid zip code"),
        ApiResponse(code = 500, message = "Unexpected error")] )
    @RequestMapping(value = ["/cep/{cep}"],method = [RequestMethod.GET])
    fun verifyCEP(@PathVariable cep: String?): ResponseEntity<CEPDto?>? {
        val restTemplate = RestTemplate()
        val url = "http://viacep.com.br/ws/$cep/json/"
        val end: CEPDto? = restTemplate
            .getForObject(url, CEPDto::class.java)

        return end?.let { ResponseEntity.ok(it) }

    }


}