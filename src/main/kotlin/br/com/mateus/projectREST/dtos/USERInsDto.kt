package br.com.mateus.projectREST.dtos

import org.hibernate.validator.constraints.NotEmpty
import org.hibernate.validator.constraints.br.CPF

data class USERInsDto (

        @get:NotEmpty(message="Name cannot be empty")
        var name: String? = null,

        @get:NotEmpty(message="Login cannot be empty")
        var login: String? = null,

        @get:NotEmpty(message="Cpf cannot be empty")
        @get:CPF(message="Cpf invalid")
        var cpf: String? = null,

        @get:NotEmpty(message="Telephone cannot be empty")
        var telephone: String? = null,

        @get:NotEmpty(message="Mail cannot be empty")
        var mail: String? = null

)