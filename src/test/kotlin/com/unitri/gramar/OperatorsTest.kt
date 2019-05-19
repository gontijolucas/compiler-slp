package com.unitri.gramar

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OperatorsTest {

    @Test
    fun `should return all operators`() {
        val validOperators = listOf("&&", "||", ">", "<", ">=", "<=", "==", "!=", "+", "-", "*", "/", ".", "=")
        val operators = Operators.operators()
        assertEquals(operators.size, validOperators.size)
        assertTrue(validOperators.containsAll(operators))
    }

    @Test
    fun `should return unary operators`() {
        val validUnaryOperators = listOf("++", "--")
        val unaryOperators = Operators.unaryOperators()
        assertEquals(validUnaryOperators.size, unaryOperators.size)
        assertTrue(validUnaryOperators.containsAll(unaryOperators))
    }

    @Test
    fun `should validate unary plus`() {
        val operator = "++"
        assertTrue(Operators.isUnaryOperator(operator))
    }

    @Test
    fun `should validate unary minus`() {
        val operator = "--"
        assertTrue(Operators.isUnaryOperator(operator))
    }

    @Test
    fun `should fail with invalid unary operator`() {
        val fakeUnaryOperators = listOf("$", "@", "", " ", "_", ".", "+-", "---", "- -")
        fakeUnaryOperators.forEach { assertFalse(Operators.isUnaryOperator(it)) }
    }

    @Test
    fun `should validate and operator`() {
        val operator = "&&"
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate or operator`() {
        val operator = "||"
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate greater then operator`() {
        val operator = ">"
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate less then operator`() {
        val operator = "<"
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate greater then or equals to operator`() {
        val operator = ">="
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate less then or equals to operator`() {
        val operator = "<="
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate equals operator`() {
        val operator = "=="
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate not equals operator`() {
        val operator = "!="
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate addition operator`() {
        val operator = "+"
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate subtraction operator`() {
        val operator = "-"
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate multiplication operator`() {
        val operator = "*"
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate division operator`() {
        val operator = "/"
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate concatenation operator`() {
        val operator = "."
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate attribution operator`() {
        val operator = "="
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate unary plus operator`() {
        val operator = "++"
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should validate unary minus operator`() {
        val operator = "--"
        assertTrue(Operators.isOperator(operator))
    }

    @Test
    fun `should fails with invalid operators`() {
        val fakeUnaryOperators = listOf("$", "@", "", " ", "_", ".-", "+-", "---", "- -")
        fakeUnaryOperators.forEach { assertFalse(Operators.isOperator(it)) }
    }

    @Test
    fun `should return default operators`() {
        val validOperators = listOf("&&", "||", ">", "<", ">=", "<=", "==", "!=", "+", "-", "*", "/", ".", "=")
        validOperators.forEach { assertTrue(Operators.isDefaultOperator(it)) }
    }

    @Test
    fun `should fail with invalid default operator`() {
        val invalidOperators = listOf("--", "++", "$", "", " ")
        invalidOperators.forEach { assertFalse(Operators.isDefaultOperator(it)) }
    }
}