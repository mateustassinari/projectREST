package br.com.mateus.projectREST.dtos

import jakarta.validation.constraints.NotBlank

data class ValidTokenDto (

        @get:NotBlank(message="Mandatory completion")
        var token: String? = ""

)
