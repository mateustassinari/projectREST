package br.com.mateus.projectREST.dtos

import org.hibernate.validator.constraints.br.CPF
import org.hibernate.validator.constraints.NotEmpty

data class USERDto (

        var id: Int? = null,

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
        var mail: String? = null,

        var status: String? = null

)