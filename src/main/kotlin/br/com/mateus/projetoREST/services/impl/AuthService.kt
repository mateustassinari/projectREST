package br.com.mateus.projetoREST.services.impl

import br.com.mateus.projetoREST.dtos.ValidTokenDto
import br.com.mateus.projetoREST.entities.USEREntity
import br.com.mateus.projetoREST.services.USERService
import br.com.mateus.projetoREST.utils.Security
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(val userService: USERService) {

    fun refreshPassword(senha: String?, login: String?): Boolean {

        val salt = Security.salt(128)
        val hash = Security.hash(senha!!,salt)

        val user: Optional<USEREntity> = userService.findUserByLogin(login!!)
        return if(user.isEmpty) {
            false
        } else {
            user.orElse(null).hash = hash
            user.orElse(null).salt = salt
            userService.persist(user.orElse(null))
            true
        }
    }

    fun validLogin(login: String?): Boolean {
        val obj: Optional<USEREntity> = userService.findUserByLoginToken(login!!)
        return if(!obj.isEmpty) {
            obj.orElseGet(null).status=="A"
        } else {
            false
        }
    }

    fun convertValidTokenDto(obj: String): ValidTokenDto? = ValidTokenDto(obj)

}