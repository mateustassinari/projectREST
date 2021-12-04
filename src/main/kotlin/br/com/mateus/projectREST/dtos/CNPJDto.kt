package br.com.mateus.projectREST.dtos

import org.hibernate.validator.constraints.NotEmpty

data class CNPJDto (

        @get:NotEmpty(message="Mandatory completion")
        var cnpj: String? = null

)