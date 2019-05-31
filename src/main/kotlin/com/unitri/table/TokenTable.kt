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
            .format("| %15s | %15s | %5s | %5s |\n", "CLASS", "VALUE", "INDEX", "COLUMN")
            .toString()

        val lineSize = content.length
        content += Formatter().format("\n".padStart(lineSize, '-'))

        tokens.forEach {
            content += Formatter()
                .format("| %15s | %15s | %5s | %5s |\n", it.tokenClass, it.image, it.line, it.column)
                .toString()
        }

        content += Formatter().format("\n".padStart(lineSize, '-'))

        return content
    }

}