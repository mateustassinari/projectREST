package br.com.mateus.projectREST.response

data class Response<T> (
        var errors: ArrayList<String> = arrayListOf(),
        var data: T? = null
)