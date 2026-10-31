package br.com.mateus.projectREST.services.impl

import br.com.mateus.projectREST.dtos.ValidTokenDto
import br.com.mateus.projectREST.security.JWTUtil
import br.com.mateus.projectREST.services.USERService
import br.com.mateus.projectREST.utils.Security
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class AuthService(val userService: USERService, val jwtUtil: JWTUtil) {

    fun refreshToken(): ValidTokenDto? {
        val user = UserService().authenticated()!!
        if (!validLogin(user.login)) {
            return null
        }
        return convertValidTokenDto(jwtUtil.generateToken(user.username))
    }

    fun refreshPassword(senha: String?, login: String?): Boolean {

        val salt = Security.salt(128)
        val hash = Security.hash(senha!!,salt)

        val user = userService.findUserByLogin(login!!).getOrNull() ?: return false
        user.hash = hash
        user.salt = salt
        userService.persist(user)
        return true
    }

    private fun validLogin(login: String?): Boolean = userService.findUserByLogin(login!!).getOrNull()?.status == "A"

    private fun convertValidTokenDto(obj: String): ValidTokenDto = ValidTokenDto(obj)

}
