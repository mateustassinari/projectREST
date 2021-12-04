package br.com.mateus.projetoREST.filters

import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletResponse


@Component
class HeaderExposureFilter: Filter {

    @Throws(IOException::class,ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val res = response as HttpServletResponse
        res.addHeader("access-control-expose-headers", "location")
        res.addHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
        res.addHeader("x-content-type-options", "nosniff")
        res.addHeader("Feature-Policy", "* 'self'")
        res.addHeader("Referrer-Policy", "origin")
        chain!!.doFilter(request, response)
    }

}