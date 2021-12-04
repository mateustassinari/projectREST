package br.com.mateus.projetoREST.response

data class Response<T> (
        var erros: ArrayList<String> = arrayListOf(),
        var data: T? = null
)