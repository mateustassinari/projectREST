package br.com.mateus.projectREST.dtos

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.br.CPF

data class USERInsDto (

        @get:NotBlank(message="Name cannot be empty")
        var name: String? = null,

        @get:NotBlank(message="Login cannot be empty")
        var login: String? = null,

        @get:NotBlank(message="Cpf cannot be empty")
        @get:CPF(message="Cpf invalid")
        var cpf: String? = null,

        @get:NotBlank(message="Telephone cannot be empty")
        var telephone: String? = null,

        @get:NotBlank(message="Mail cannot be empty")
        var mail: String? = null

)
