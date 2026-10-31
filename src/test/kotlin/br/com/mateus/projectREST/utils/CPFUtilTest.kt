package br.com.mateus.projectREST.utils

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CPFUtilTest {

    @Test
    fun `accepts valid CPFs with and without punctuation`() {
        listOf("52998224725", "529.982.247-25", "11144477735", "390.533.447-05").forEach {
            assertTrue(CPFUtil.isValid(it), "should accept $it")
        }
    }

    @Test
    fun `rejects wrong check digits`() {
        listOf("52998224726", "52998224735", "12345678900").forEach {
            assertFalse(CPFUtil.isValid(it), "should reject $it")
        }
    }

    @Test
    fun `rejects repeated digits`() {
        (0..9).map { it.toString().repeat(11) }.forEach {
            assertFalse(CPFUtil.isValid(it), "should reject $it")
        }
    }

    @Test
    fun `rejects wrong length and non digits`() {
        listOf("", "5299822472", "529982247250", "5299822472A", "+2998224725", "abcdefghijk").forEach {
            assertFalse(CPFUtil.isValid(it), "should reject '$it'")
        }
    }
}
