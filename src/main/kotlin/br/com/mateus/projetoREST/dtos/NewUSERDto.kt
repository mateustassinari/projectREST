package br.com.mateus.projetoREST.dtos

import br.com.mateus.projetoREST.entities.USEREntity

data class NewUSERDto (

    var user: USEREntity? = null,
    var newPass: String? = null

)