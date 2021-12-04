package br.com.mateus.projetoREST.security

import br.com.mateus.projetoREST.utils.Security
import org.springframework.security.crypto.password.PasswordEncoder

class Password : PasswordEncoder {

    override fun encode(rawPassword: CharSequence?): String {
        val salt = Security.salt(128)
        return Security.hash(rawPassword.toString(), salt)
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        val salt = encodedPassword?.split("|_|")?.get(0)
        val hash = encodedPassword?.split("|_|")?.get(1)
        return Security.test(rawPassword as String,salt!!,hash!!)
    }

}