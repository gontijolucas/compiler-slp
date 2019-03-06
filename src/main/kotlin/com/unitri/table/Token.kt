package com.unitri.table

import com.unitri.Delimiters
import com.unitri.Keywords
import com.unitri.Operators

data class Token(
    val tokenClass: TokenClass,
    val value: String,
    val index: Int?,
    val column: Int?
) {

    enum class TokenClass(val value: String) {

        INTEGER_LITERAL("Literal inteiro"),
        FLOAT_LITERAL("Literal real"),
        LOGIC_LITERAL("Literal lógico"),
        STRING_LITERAL("Constante literal String"),
        DELIMITER("Delimitador"),
        KEYWORD("Palavra reservada"),
        OPERATOR("Operador"),
        IDENTIFIER("Identificador");

        companion object {

            fun getClass(letter: String): TokenClass {
                return when {
                    letter.matches("([0-9]+)".toRegex()) -> INTEGER_LITERAL
                    letter.matches("([0-9]+.[0-9]+)".toRegex()) -> FLOAT_LITERAL
                    letter.matches("(vero|falso)".toRegex()) -> LOGIC_LITERAL
                    letter.matches("\".*\"".toRegex()) -> STRING_LITERAL
                    Delimiters.isDelimiter(letter) -> DELIMITER
                    Keywords.isKeyword(letter) -> KEYWORD
                    Operators.isOperator(letter) -> OPERATOR
                    else -> TokenClass.IDENTIFIER
                }
            }
        }
    }

}

