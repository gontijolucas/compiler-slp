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
        fun types() = listOf("int", "real", "texto", "logico", "nada")
    }
}

enum class Operators(private val value: String) {
    AND("&&"),
    OR("||"),
    GREATER_THEN(">"),
    LESS_THEN("<"),
    GREATER_THEN_EQUALS_TO(">="),
    LESS_THEN_EQUALS_TO("<="),
    EQUALS("=="),
    NOT_EQUALS("!="),
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/"),
    CONCATENATION("."),
    ATTRIBUTION("="),
    UNARY_PLUS("++"),
    UNARY_MINUS("--");

    companion object {
        fun isOperator(symbol: String): Boolean = values().any { it.value == symbol }
        fun unaryOperators() = listOf(UNARY_PLUS.value, UNARY_MINUS.value)
        fun operators() = listOf(
            AND.value,
            OR.value,
            GREATER_THEN.value,
            LESS_THEN.value,
            GREATER_THEN_EQUALS_TO.value,
            LESS_THEN_EQUALS_TO.value,
            EQUALS.value,
            NOT_EQUALS.value,
            ADDITION.value,
            SUBTRACTION.value,
            MULTIPLICATION.value,
            DIVISION.value,
            CONCATENATION.value,
            ATTRIBUTION.value
        )
    }
}

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