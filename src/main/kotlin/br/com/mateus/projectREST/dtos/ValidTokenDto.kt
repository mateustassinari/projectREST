package br.com.mateus.projectREST.dtos

import org.hibernate.validator.constraints.NotEmpty

data class ValidTokenDto (

        @get:NotEmpty(message="Mandatory completion")
        var token: String? = ""

)