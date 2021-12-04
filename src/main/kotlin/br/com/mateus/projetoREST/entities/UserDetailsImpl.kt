package br.com.mateus.projetoREST.entities

import br.com.mateus.projetoREST.security.UserSS
import br.com.mateus.projetoREST.services.USERService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDetailsImpl(private val user: USERService) : UserDetailsService {

    override fun loadUserByUsername(login: String?): UserDetails {
        val obj: Optional<USEREntity> = user.findUserByLogin(login!!)
        return if(obj.isEmpty) {
               throw Exception("User not found")
        } else {
            UserSS(obj.orElse(null).login, obj.orElse(null).salt + "|_|" + obj.orElse(null).hash,obj.orElse(null).status)
        }
    }
}
