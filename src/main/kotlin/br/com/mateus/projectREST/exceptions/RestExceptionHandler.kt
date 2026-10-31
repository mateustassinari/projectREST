package br.com.mateus.projectREST.exceptions

import br.com.mateus.projectREST.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(e: ConflictException): ResponseEntity<Response<Void>> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(Response(arrayListOf(e.message!!)))

    @ExceptionHandler(InvalidDataException::class)
    fun handleInvalidData(e: InvalidDataException): ResponseEntity<Response<Void>> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response(arrayListOf(e.message!!)))

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(e: NotFoundException): ResponseEntity<Void> =
        ResponseEntity.notFound().build()

    @ExceptionHandler(ServiceUnavailableException::class)
    fun handleServiceUnavailable(e: ServiceUnavailableException): ResponseEntity<Response<Void>> =
        ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Response(arrayListOf(e.message!!)))
}
