package br.com.mateus.projectREST.dtos

import jakarta.validation.constraints.NotBlank

data class LoginDto (

        @get:NotBlank(message="Mandatory completion")
        var login: String? = null,

        @get:NotBlank(message="Mandatory completion")
        var password: String? = null

)
