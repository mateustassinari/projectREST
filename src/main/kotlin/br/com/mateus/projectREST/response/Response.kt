package br.com.mateus.projectREST.response

data class Response<T> (
        var erros: ArrayList<String> = arrayListOf(),
        var data: T? = null
)