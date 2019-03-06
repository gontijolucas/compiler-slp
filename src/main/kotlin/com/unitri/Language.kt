package com.unitri

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
        fun isKeyword(symbol: String): Boolean = Keywords.values().any { it.value == symbol }
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
    ATTRIBUTION("=");

    companion object {
        fun isOperator(symbol: String): Boolean = Operators.values().any { it.value == symbol }
    }
}

enum class Delimiters(private val value: String) {
    END_LINE("$"),
    OPEN_SCOPE("("),
    CLOSE_SCOPE(")"),
    OPEN_PARAMETERS("["),
    CLOSE_PARAMETERS("]"),
    SEPARATE_PARAMETERS(",");

    companion object {
        fun isDelimiter(symbol: String): Boolean = Delimiters.values().any { it.value == symbol }
    }
}