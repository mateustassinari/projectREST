package br.com.mateus.projetoREST.services.impl

import br.com.mateus.projetoREST.security.UserSS
import org.springframework.security.core.context.SecurityContextHolder


class UserService {

    fun authenticated(): UserSS? {
        return try {
            SecurityContextHolder.getContext().authentication.principal as UserSS
        } catch (e: Exception) {
            null
        }
    }

}