package br.com.mateus.projectREST.services.impl

import br.com.mateus.projectREST.security.UserSS
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