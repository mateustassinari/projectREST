package br.com.mateus.projectREST.controllers

import br.com.mateus.projectREST.dtos.*
import br.com.mateus.projectREST.response.Response
import br.com.mateus.projectREST.services.USERService
import br.com.mateus.projectREST.services.impl.CepService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder


@RestController
@RequestMapping("/user")
class USERController(val userService: USERService, val cepService: CepService) {

    @Operation(summary = "Return User by login")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successful request"),
        ApiResponse(responseCode = "401", description = "Not authorized"),
        ApiResponse(responseCode = "404", description = "Not found"),
        ApiResponse(responseCode = "400", description = "Parameter not informed"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(method = [RequestMethod.GET])
    fun findUser(@RequestParam(value="login") login: String?): ResponseEntity<USERDto> {
        if (login == null) {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok(userService.findByLogin(login))
    }

    @Operation(summary = "Return User by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successful request"),
        ApiResponse(responseCode = "401", description = "Not authorized"),
        ApiResponse(responseCode = "404", description = "Not found"),
        ApiResponse(responseCode = "400", description = "Parameter not informed"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun findUserId(@PathVariable id: Int): ResponseEntity<USERDto> =
        ResponseEntity.ok(userService.findById(id))

    @Operation(summary = "Register a new User")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "User Created - New resource URI in header"),
        ApiResponse(responseCode = "409", description = "User already registered"),
        ApiResponse(responseCode = "400", description = "Lack of information/poorly formatted request"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(method = [RequestMethod.POST])
    fun addUser(@Valid @RequestBody userDto: USERInsDto): ResponseEntity<Response<ReturnUSERDto>> {
        val created = userService.register(userDto)
        val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.idUser).toUri()
        return ResponseEntity.created(uri).body(Response(data = created))
    }

    @Operation(summary = "Update a User")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Updated User"),
        ApiResponse(responseCode = "401", description = "Not authorized"),
        ApiResponse(responseCode = "400", description = "Lack of information/poorly formatted request"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.PUT])
    fun updateUser(@Valid @RequestBody userDto: USERUpdDto, @PathVariable id: Int): ResponseEntity<Response<Void>> {
        userService.updateUser(id, userDto)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Delete a User")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Deleted User"),
        ApiResponse(responseCode = "401", description = "Not authorized"),
        ApiResponse(responseCode = "400", description = "Lack of information/poorly formatted request"),
        ApiResponse(responseCode = "500", description = "Unexpected error")])
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.DELETE])
    fun delUser(@PathVariable id: Int): ResponseEntity<Response<Void>> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Get address through zip code")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successful request"),
        ApiResponse(responseCode = "400", description = "Lack of information or invalid zip code"),
        ApiResponse(responseCode = "404", description = "Zip code not found"),
        ApiResponse(responseCode = "500", description = "Unexpected error"),
        ApiResponse(responseCode = "503", description = "Zip code service unavailable")])
    @RequestMapping(value = ["/cep/{cep}"],method = [RequestMethod.GET])
    fun verifyCEP(@PathVariable cep: String): ResponseEntity<CEPDto> =
        ResponseEntity.ok(cepService.findAddress(cep))
}
