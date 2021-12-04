package br.com.mateus.projectREST.dtos

import org.hibernate.validator.constraints.NotEmpty

data class PasswDto (

        @get:NotEmpty(message="Mandatory completion")
        var login: String? = null,

        @get:NotEmpty(message="Mandatory completion")
        var password: String? = null

)