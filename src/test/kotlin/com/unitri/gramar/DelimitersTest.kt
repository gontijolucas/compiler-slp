package com.unitri.gramar

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DelimitersTest {

    @Test
    fun `should validate end line delimiter`() {
        val delimiter = "$"
        assertTrue(Delimiters.isDelimiter(delimiter))
    }

    @Test
    fun `should validate open scope delimiter`() {
        val delimiter = "("
        assertTrue(Delimiters.isDelimiter(delimiter))
    }

    @Test
    fun `should validate closen scope delimiter`() {
        val delimiter = ")"
        assertTrue(Delimiters.isDelimiter(delimiter))
    }

    @Test
    fun `should validate open parameters delimiter`() {
        val delimiter = "["
        assertTrue(Delimiters.isDelimiter(delimiter))
    }

    @Test
    fun `should validate close parameters delimiter`() {
        val delimiter = "]"
        assertTrue(Delimiters.isDelimiter(delimiter))
    }

    @Test
    fun `should not validate an invalid delimiter`() {
        val invalidDelimiters = listOf("@", "", "a", "[@", "[]", "][", "_", " ")
        invalidDelimiters.forEach { assertFalse(Delimiters.isDelimiter(it)) }
    }
}