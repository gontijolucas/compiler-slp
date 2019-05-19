package com.unitri.gramar

enum class Keywords(private val value: String) {
    FUNCTION("fun"),
    INTEGER("int"),
    FLOAT("real"),
    STRING("texto"),
    LOGICAL("logico"),
    VOID("nada"),
    WHILE("enquanto"),
    IF("se"),
    ELSE("senao"),
    RETURN("retorna"),
    RETURN_FUNCTION(":");

    companion object {
        fun isKeyword(symbol: String): Boolean = values().any { it.value == symbol }
        fun types() = listOf(INTEGER.value, FLOAT.value, STRING.value, LOGICAL.value, VOID.value)
    }
}