package br.com.mateus.projectREST.exceptions

open class BusinessException(message: String) : RuntimeException(message)

class ConflictException(message: String) : BusinessException(message)

class InvalidDataException(message: String) : BusinessException(message)

class NotFoundException(message: String = "Not found") : BusinessException(message)

class ServiceUnavailableException(message: String) : BusinessException(message)
