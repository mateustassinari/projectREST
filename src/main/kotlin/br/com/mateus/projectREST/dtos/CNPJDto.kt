package br.com.mateus.projectREST.dtos

import jakarta.validation.constraints.NotBlank

data class CNPJDto (

        @get:NotBlank(message="Mandatory completion")
        var cnpj: String? = null

)
