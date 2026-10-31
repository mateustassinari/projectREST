package br.com.mateus.projectREST.services.impl

import br.com.mateus.projectREST.dtos.NewUSERDto
import br.com.mateus.projectREST.dtos.ReturnUSERDto
import br.com.mateus.projectREST.exceptions.ConflictException
import br.com.mateus.projectREST.exceptions.InvalidDataException
import br.com.mateus.projectREST.exceptions.NotFoundException
import br.com.mateus.projectREST.utils.CPFUtil
import br.com.mateus.projectREST.utils.Security
import br.com.mateus.projectREST.utils.NewPassword
import br.com.mateus.projectREST.dtos.USERDto
import br.com.mateus.projectREST.dtos.USERInsDto
import br.com.mateus.projectREST.dtos.USERUpdDto
import br.com.mateus.projectREST.entities.USEREntity
import br.com.mateus.projectREST.repositories.USERRepository
import br.com.mateus.projectREST.security.UserSS
import br.com.mateus.projectREST.services.USERService
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class USERServiceImpl(val userRepository: USERRepository): USERService {

    private val onlyDigits = "^[0-9]*$".toRegex()
    private val mailPattern = "^([\\w.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)\$".toRegex()

    override fun findUserById(id: Int): Optional<USEREntity> = userRepository.findById(id)

    override fun findUserByLogin(login: String): Optional<USEREntity> = userRepository.findByLogin(login)

    override fun findUserByName(name: String): Optional<USEREntity> = userRepository.findByNameUser(name)

    override fun findUserByCPF(cpf: String): Optional<USEREntity> = userRepository.findByCpf(cpf)

    override fun findUserByMail(mail: String): Optional<USEREntity> = userRepository.findByMail(mail)

    override fun findUserByTelephone(telephone: String): Optional<USEREntity> = userRepository.findByTelephone(telephone)

    @Transactional
    override fun persist(user: USEREntity): USEREntity = userRepository.save(user)

    @Transactional
    override fun delete(id: Int): Unit = userRepository.deleteById(id)

    @Transactional
    override fun update(user: USEREntity): USEREntity {

        val obj: USEREntity = userRepository.findById(user.idUser!!).orElseThrow { NotFoundException() }
        if(user.nameUser != null) {
            obj.nameUser = user.nameUser
        }
        if(user.telephone != null) {
            obj.telephone = user.telephone
        }

        return userRepository.save(obj)

    }

    override fun findByLogin(login: String): USERDto {
        val user = findUserByLogin(login).orElseThrow { NotFoundException() }
        val authenticated: UserSS? = UserService().authenticated()
        if (authenticated != null && login != authenticated.login) {
            throw AuthorizationServiceException("Access denied!")
        }
        return convertUSERDto(user)
    }

    override fun findById(id: Int): USERDto = convertUSERDto(findUserById(id).orElseThrow { NotFoundException() })

    override fun register(userDto: USERInsDto): ReturnUSERDto {
        if (findUserByLogin(userDto.login!!).isPresent) {
            throw ConflictException("Login already registered!")
        }
        if (findUserByCPF(userDto.cpf!!).isPresent) {
            throw ConflictException("CPF already registered!")
        }
        if (userDto.mail != null && findUserByMail(userDto.mail!!).isPresent) {
            throw ConflictException("Mail already registered!")
        }

        if (!CPFUtil.isValid(userDto.cpf!!)) {
            throw InvalidDataException("CPF invalid!")
        }
        validateTelephone(userDto.telephone!!)
        if (!userDto.mail!!.matches(mailPattern)) {
            throw InvalidDataException("Mail is not valid!")
        }

        val obj: NewUSERDto = convertDtoToNewUSER(userDto)
        val saved = persist(obj.user!!)
        return ReturnUSERDto(saved.idUser, obj.newPass)
    }

    override fun updateUser(id: Int, userDto: USERUpdDto) {
        if (userDto.name == null && userDto.telephone == null) {
            throw InvalidDataException("Inform name or telephone to update!")
        }
        userDto.telephone?.let { validateTelephone(it) }

        val obj: USEREntity = convertDtoToUSER(userDto)
        obj.idUser = id
        update(obj)
    }

    override fun deleteUser(id: Int) {
        if (findUserById(id).isEmpty) {
            throw NotFoundException()
        }
        delete(id)
    }

    private fun validateTelephone(telephone: String) {
        if (!telephone.matches(onlyDigits)) {
            throw InvalidDataException("Telephone does not contain only numbers!")
        }
    }

    private fun convertUSERDto(obj: USEREntity): USERDto = USERDto(obj.idUser,obj.nameUser,obj.login,obj.cpf,obj.telephone,obj.mail,obj.status)

    private fun convertDtoToUSER(updDto: USERUpdDto): USEREntity = USEREntity(null,updDto.name, null,null,updDto.telephone,null,null,null,null)

    private fun convertDtoToNewUSER(insDto: USERInsDto): NewUSERDto {

        val newPass: String = NewPassword().newPassword()
        val salt = Security.salt(128)
        val hash = Security.hash(newPass,salt)
        return NewUSERDto(USEREntity(null,insDto.name, insDto.login, insDto.cpf, insDto.telephone,insDto.mail,"A",hash,salt),newPass)

    }


}
