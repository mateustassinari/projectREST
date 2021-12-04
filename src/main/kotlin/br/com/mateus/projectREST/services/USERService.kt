package br.com.mateus.projectREST.services

import br.com.mateus.projectREST.dtos.NewUSERDto
import br.com.mateus.projectREST.dtos.USERDto
import br.com.mateus.projectREST.dtos.USERInsDto
import br.com.mateus.projectREST.dtos.USERUpdDto
import br.com.mateus.projectREST.entities.USEREntity
import java.util.*

interface USERService {

    fun findUserById(id: Int): Optional<USEREntity>

    fun findUserByLogin(login: String): Optional<USEREntity>

    fun findUserByLoginToken(login: String): Optional<USEREntity>

    fun findUserByName(name: String): Optional<USEREntity>

    fun findUserByCPF(cpf: String): Optional<USEREntity>

    fun findUserByMail(mail: String): Optional<USEREntity>

    fun findUserByTelephone(telephone: String): Optional<USEREntity>

    fun persist(user: USEREntity): USEREntity

    fun delete(id: Int)

    fun update(user: USEREntity): USEREntity

    fun convertUSERDto(obj: USEREntity): USERDto?

    fun convertDtoToUSER(USERUpdDto: USERUpdDto): USEREntity

    fun convertDtoToNewUSER(USERDto: USERInsDto): NewUSERDto

}