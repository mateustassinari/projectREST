package br.com.mateus.projetoREST.services.impl

import br.com.mateus.projetoREST.dtos.NewUSERDto
import br.com.mateus.projetoREST.utils.Security
import br.com.mateus.projetoREST.utils.NewPassword
import br.com.mateus.projetoREST.dtos.USERDto
import br.com.mateus.projetoREST.dtos.USERInsDto
import br.com.mateus.projetoREST.dtos.USERUpdDto
import br.com.mateus.projetoREST.entities.USEREntity
import br.com.mateus.projetoREST.repositories.USERRepository
import br.com.mateus.projetoREST.security.UserSS
import br.com.mateus.projetoREST.services.USERService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class USERServiceImpl(val userRepository: USERRepository): USERService {

    override fun findUserById(id: Int): Optional<USEREntity> = userRepository.findById(id)

    override fun findUserByLoginToken(login: String): Optional<USEREntity> {
        val user: UserSS? = UserService().authenticated()
        return userRepository.findByLogin(login)
    }

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

        val obj: Optional<USEREntity> = userRepository.findById(user.idUser!!)
        if(user.nameUser != null) {
            obj.orElse(null).nameUser = user.nameUser
        }
        if(user.telephone != null) {
            obj.orElse(null).telephone = user.telephone
        }

        return userRepository.save(obj.orElse(null))

    }

    override fun convertUSERDto(obj: USEREntity): USERDto? = USERDto(obj.idUser,obj.nameUser,obj.login,obj.cpf,obj.telephone,obj.mail,obj.status)

    override fun convertDtoToUSER(USERUpdDto: USERUpdDto): USEREntity = USEREntity(null,USERUpdDto.name, null,null,USERUpdDto.telephone,null,null,null,null)

    override fun convertDtoToNewUSER(USERDto: USERInsDto): NewUSERDto {

        val newPass: String = NewPassword().newPassword()
        val salt = Security.salt(128)
        val hash = Security.hash(newPass,salt)
        return NewUSERDto(USEREntity(null,USERDto.name, USERDto.login, USERDto.cpf, USERDto.telephone,USERDto.mail,"A",hash,salt),newPass)

    }


}