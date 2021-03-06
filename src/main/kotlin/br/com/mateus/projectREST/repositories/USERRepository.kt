package br.com.mateus.projectREST.repositories

import br.com.mateus.projectREST.entities.USEREntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface USERRepository: JpaRepository<USEREntity, Int> {

    fun findByLogin(login: String): Optional<USEREntity>

    fun findByNameUser(name: String): Optional<USEREntity>

    fun findByCpf(cpf: String): Optional<USEREntity>

    fun findByTelephone(telephone: String): Optional<USEREntity>

    fun findByMail(mail: String): Optional<USEREntity>


}