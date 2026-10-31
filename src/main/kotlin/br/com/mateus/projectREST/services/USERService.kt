package br.com.mateus.projectREST.services

import br.com.mateus.projectREST.dtos.ReturnUSERDto
import br.com.mateus.projectREST.dtos.USERDto
import br.com.mateus.projectREST.dtos.USERInsDto
import br.com.mateus.projectREST.dtos.USERUpdDto
import br.com.mateus.projectREST.entities.USEREntity
import java.util.*

interface USERService {

    fun findUserById(id: Int): Optional<USEREntity>

    fun findUserByLogin(login: String): Optional<USEREntity>

    fun findUserByName(name: String): Optional<USEREntity>

    fun findUserByCPF(cpf: String): Optional<USEREntity>

    fun findUserByMail(mail: String): Optional<USEREntity>

    fun findUserByTelephone(telephone: String): Optional<USEREntity>

    fun persist(user: USEREntity): USEREntity

    fun delete(id: Int)

    fun update(user: USEREntity): USEREntity

    fun findByLogin(login: String): USERDto

    fun findById(id: Int): USERDto

    fun register(userDto: USERInsDto): ReturnUSERDto

    fun updateUser(id: Int, userDto: USERUpdDto)

    fun deleteUser(id: Int)

}
