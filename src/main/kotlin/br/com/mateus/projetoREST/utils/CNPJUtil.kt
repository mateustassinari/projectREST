package br.com.mateus.projetoREST.utils

class CNPJUtil {
    companion object {

        fun myValidateCNPJ(CNPJ : String) : Boolean{
            val cnpj = CNPJ
            return validateCNPJLength(cnpj) && validateCNPJRepeatedNumbers(cnpj)
                    && validateCNPJVerificationDigit(true, cnpj)
                    && validateCNPJVerificationDigit(false, cnpj)
        }

        /**
         * Verifies if the CNPJ has 14 digits.
         *
         * @return True if valid.
         */
        private fun validateCNPJLength(cnpj: String) = cnpj.length == 14

        /**
         * Verifies if the CNPJ is not repeated numbers.
         *
         * A CNPJ with repeated is considered invalid, ex:
         *
         *   '00000000000000'
         *   '11111111111111'
         *   '22222222222222'
         *   ...
         *   '88888888888888'
         *   '99999999999999'
         *
         * @return True if valid.
         */
        private fun validateCNPJRepeatedNumbers(cnpj: String): Boolean {
            return (0..9)
                    .map { it.toString().repeat(14) }
                    .map { cnpj == it }
                    .all { !it }
        }

        /**
         * Verifies the CNPJ verification digit.
         *
         * This algorithm checks the verification digit (dígito verificador) do CNPJ.
         * This was based from: https://www.devmedia.com.br/validando-o-cnpj-em-uma-aplicacao-java/22374
         *
         * @param[firstDigit] True when checking the first digit. False to check the second digit.
         *
         * @return True if valid.
         */
        private fun validateCNPJVerificationDigit(firstDigit: Boolean, cnpj: String): Boolean {
            val startPos = when (firstDigit) {
                true -> 11
                else -> 12
            }
            val weightOffset = when (firstDigit) {
                true -> 0
                false -> 1
            }
            val sum = (startPos downTo 0).fold(0) { acc, pos ->
                val weight = 2 + ((11 + weightOffset - pos) % 8)
                val num = cnpj[pos].toString().toInt()
                val sum = acc + (num * weight)
                sum
            }
            val expectedDigit = when (val result = sum % 11) {
                0, 1 -> 0
                else -> 11 - result
            }

            val actualDigit = cnpj[startPos + 1].toString().toInt()

            return expectedDigit == actualDigit
        }
    }
}