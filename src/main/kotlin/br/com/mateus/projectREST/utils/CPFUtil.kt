package br.com.mateus.projectREST.utils

object CPFUtil {

    /** Accepts 11 digits, with or without the usual "." and "-" punctuation. */
    fun isValid(cpf: String): Boolean {
        val digits = cpf.replace(".", "").replace("-", "")
        if (digits.length != 11 || digits.any { it !in '0'..'9' } || digits.all { it == digits[0] }) {
            return false
        }
        val numbers = digits.map { it - '0' }
        return checkDigit(numbers, 9) == numbers[9] && checkDigit(numbers, 10) == numbers[10]
    }

    /** Check digit for the first [length] digits, using weights [length] + 1 down to 2. */
    private fun checkDigit(numbers: List<Int>, length: Int): Int {
        val sum = (0 until length).sumOf { numbers[it] * (length + 1 - it) }
        return sum * 10 % 11 % 10
    }
}
