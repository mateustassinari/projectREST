package br.com.mateus.projectREST.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


 data class UserSS  (
    var login: String? = null,
    var senha: String? = null,
    var ativo: String? = null
): UserDetails {

    override fun getPassword(): String {
        return senha!!
    }

    override fun getUsername(): String {
        return login!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf<GrantedAuthority>()
    }

    override fun isEnabled(): Boolean {
        return true
    }

}