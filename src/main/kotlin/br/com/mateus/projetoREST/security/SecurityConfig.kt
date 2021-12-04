package br.com.mateus.projetoREST.security

import br.com.mateus.projetoREST.services.USERService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtUtil: JWTUtil

    @Autowired
    private lateinit var user: USERService

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**",
                "/swagger-ui.html", "/webjars/**")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST,"/user").permitAll()
                .antMatchers("/user/cep/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));


        http.addFilter(getJWTAuthenticationFilter())
        http.addFilter(JWTAuthorizationFilter(authenticationManager(), jwtUtil = jwtUtil, userDetailService = userDetailsService))
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun getJWTAuthenticationFilter(): JWTAuthenticationFilter? {
        val filter = JWTAuthenticationFilter(authenticationManager(), jwtUtil = jwtUtil, user = user)
        filter.setFilterProcessesUrl("/auth/login")
        return filter
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration().applyPermitDefaultValues()
        configuration.allowedMethods = listOf("POST", "GET", "PUT", "DELETE", "OPTIONS")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return Password()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }

}