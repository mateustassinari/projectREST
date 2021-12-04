package br.com.mateus.projetoREST.utils

import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object Security {
    private const val interactions = 13
    private const val keyLength = 512
    private const val saltLength = 128
    @JvmOverloads
    fun salt(saltLength: Int = Security.saltLength): String {
        val random = SecureRandom()
        val saltBytes = ByteArray(saltLength)
        random.setSeed(System.currentTimeMillis())
        random.nextBytes(saltBytes)
        return Base64.getEncoder().encodeToString(saltBytes)
    }

    fun hash(password: String, salt: String): String {
        val hash = computeHash(password, salt.toByteArray(), interactions, keyLength)
        return Base64.getEncoder().encodeToString(hash)
    }

    private fun computeHash(password: String, salt: ByteArray, interactions: Int, keylen: Int): ByteArray? {
        val id = "PBKDF2WithHmacSHA512"
        val passwordArray = password.toCharArray()
        val keySpec = PBEKeySpec(passwordArray, salt, interactions, keylen * 8)
        try {
            val keyFactory = SecretKeyFactory.getInstance(id)
            return keyFactory.generateSecret(keySpec).encoded
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun test(password: String, salt: String, hash: String): Boolean {
        return hash(password, salt) == hash
    }
}