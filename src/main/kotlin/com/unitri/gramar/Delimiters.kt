package com.unitri.gramar

enum class Delimiters(private val value: String) {
    END_LINE("$"),
    OPEN_SCOPE("("),
    CLOSE_SCOPE(")"),
    OPEN_PARAMETERS("["),
    CLOSE_PARAMETERS("]");

    companion object {
        fun isDelimiter(symbol: String): Boolean = values().any { it.value == symbol }
    }
}