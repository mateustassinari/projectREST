package br.com.mateus.projectREST.dtos

import br.com.mateus.projectREST.entities.USEREntity

data class NewUSERDto (

    var user: USEREntity? = null,
    var newPass: String? = null

)