package br.com.mateus.projectREST.dtos

data class CEPDto (

        var cep: String? = null,

        var logradouro: String? = null,

        var complemento: String? = null,

        var bairro: String? = null,

        var localidade: String? = null,

        var uf: String? = null,

        var unidade: String? = null,

        var ibge: String? = null,

        var gia: String? = null

)