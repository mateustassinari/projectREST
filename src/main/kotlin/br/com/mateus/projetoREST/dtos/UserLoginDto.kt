package br.com.mateus.projetoREST.dtos

data class UserLoginDto(

        var id: Int? = null,
        var name: String? = null,
        var login: String? = null,
        var status: String? = null,
        var token: String? = null,
        var mail: String? = null,
        var telephone: String? = null,
        var cpf: String? = null
)
