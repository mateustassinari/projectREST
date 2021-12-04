package br.com.mateus.projetoREST.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

@Entity
@Table(name = "USER", catalog = "")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler","hash","salt")
data class USEREntity (
    @get:Id
    @get:GeneratedValue(strategy=GenerationType.IDENTITY)
    @get:Column(name = "CD_USER", nullable = false, insertable = false, updatable = false)
    var idUser: Int? = null,
    @get:Basic
    @get:Column(name = "DS_USER", nullable = false)
    var nameUser: String? = null,
    @get:Basic
    @get:Column(name = "NM_USER_LOGIN", nullable = false)
    var login: String? = null,
    @get:Basic
    @get:Column(name = "DS_USER_CPF", nullable = false)
    var cpf: String? = null,
    @get:Basic
    @get:Column(name = "DS_USER_TELEF", nullable = true)
    var telephone: String? = null,
    @get:Basic
    @get:Column(name = "DS_USER_MAIL", nullable = false)
    var mail: String? = null,
    @get:Basic
    @get:Column(name = "ID_USER_STATU", nullable = true)
    var status: String? = null,
    @get:Basic
    @get:Column(name = "DS_USER_HASH", nullable = false, length = 1024)
    @ApiModelProperty(hidden= true)
    var hash: String? = null,
    @get:Basic
    @get:Column(name = "DS_USER_SALT", nullable = false, length = 1024)
    @ApiModelProperty(hidden= true)
    var salt: String? = null

) {
    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "idUser = $idUser " +
                "nameUser = $nameUser " +
                "login = $login " +
                "cpf = $cpf " +
                "telephone = $telephone " +
                "mail = $mail " +
                "status = $status " +
                "hash = $hash " +
                "salt = $salt " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as USEREntity

        if (idUser != other.idUser) return false
        if (nameUser != other.nameUser) return false
        if (login != other.login) return false
        if (cpf != other.cpf) return false
        if (telephone != other.telephone) return false
        if (mail != other.mail) return false
        if (status != other.status) return false
        if (hash != other.hash) return false
        if (salt != other.salt) return false


        return true
    }

}

