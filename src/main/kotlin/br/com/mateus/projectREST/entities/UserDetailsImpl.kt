package br.com.mateus.projectREST.entities

import br.com.mateus.projectREST.security.UserSS
import br.com.mateus.projectREST.services.USERService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserDetailsImpl(private val user: USERService) : UserDetailsService {

    override fun loadUserByUsername(login: String): UserDetails {
        val obj = user.findUserByLogin(login).getOrNull() ?: throw Exception("User not found")
        return UserSS(obj.login, obj.salt + "|_|" + obj.hash, obj.status)
    }
}
