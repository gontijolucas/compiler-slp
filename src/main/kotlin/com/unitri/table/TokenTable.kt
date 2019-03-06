package com.unitri.table

import java.util.*

data class TokenTable(
    val tokens: MutableList<Token> = mutableListOf()
) {

    fun createTokenAndPutInTable(tokenClass: Token.TokenClass, value: String, line: Int?, column: Int): Token {
        val token = Token(tokenClass, value, line, column)
        tokens.add(token)
        return token
    }

    fun addTokens(tokens: List<Token>): List<Token> {
        this.tokens.addAll(tokens)
        return this.tokens
    }

    override fun toString(): String {

        var content = Formatter()
            .format("| %15s | %10s | %10s | %10s |\n", "CLASS", "VALUE", "INDEX", "COLUMN")
            .toString()

        content += Formatter().format("\n".padStart(content.length, '-'))

        tokens.forEach {
            content += Formatter()
                .format("| %15s | %10s | %10s | %10s |\n", it.tokenClass, it.value, it.index, it.column)
                .toString()
        }

        return content
    }


}