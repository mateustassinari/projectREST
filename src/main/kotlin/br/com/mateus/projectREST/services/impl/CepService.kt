package br.com.mateus.projectREST.services.impl

import br.com.mateus.projectREST.dtos.CEPDto
import br.com.mateus.projectREST.exceptions.InvalidDataException
import br.com.mateus.projectREST.exceptions.NotFoundException
import br.com.mateus.projectREST.exceptions.ServiceUnavailableException
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Service
class CepService {

    private val restTemplate = RestTemplate(SimpleClientHttpRequestFactory().apply {
        setConnectTimeout(Duration.ofSeconds(3))
        setReadTimeout(Duration.ofSeconds(5))
    })
    private val cepPattern = "^[0-9]{5}-?[0-9]{3}$".toRegex()

    fun findAddress(cep: String): CEPDto {
        if (!cep.matches(cepPattern)) {
            throw InvalidDataException("Zip code must contain 8 digits!")
        }
        val address = try {
            restTemplate.getForObject("https://viacep.com.br/ws/${cep.replace("-", "")}/json/", CEPDto::class.java)
        } catch (_: HttpClientErrorException) {
            throw InvalidDataException("Invalid zip code!")
        } catch (_: RestClientException) {
            throw ServiceUnavailableException("Zip code service is unavailable!")
        }
        // ViaCEP answers 200 with {"erro": true} for a well-formed zip code that does not exist
        return address?.takeIf { it.cep != null } ?: throw NotFoundException()
    }
}
