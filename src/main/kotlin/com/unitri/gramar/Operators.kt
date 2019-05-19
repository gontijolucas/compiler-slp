package com.unitri.gramar

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
        fun unaryOperators() = listOf(UNARY_PLUS.value, UNARY_MINUS.value)
        fun isOperator(symbol: String): Boolean = values().any { it.value == symbol }
        fun isUnaryOperator(symbol: String): Boolean = unaryOperators().contains(symbol)
        fun isDefaultOperator(symbol: String): Boolean = operators().contains(symbol)
    }
}