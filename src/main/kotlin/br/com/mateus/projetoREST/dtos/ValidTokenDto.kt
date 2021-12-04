package br.com.mateus.projetoREST.dtos

import org.hibernate.validator.constraints.NotEmpty

data class ValidTokenDto (

        @get:NotEmpty(message="Mandatory completion")
        var token: String? = ""

)