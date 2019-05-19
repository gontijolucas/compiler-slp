package com.unitri.gramar

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KeywordsTest {

    @Test
    fun `should validate function keyword`() {
        val keyword = "fun"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should validate integer keyword`() {
        val keyword = "int"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should validate float keyword`() {
        val keyword = "real"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should validate string keyword`() {
        val keyword = "texto"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should validate logical keyword`() {
        val keyword = "logico"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should validate void keyword`() {
        val keyword = "nada"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should validate while keyword`() {
        val keyword = "enquanto"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should validate if keyword`() {
        val keyword = "se"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should validate else keyword`() {
        val keyword = "senao"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should validate return keyword`() {
        val keyword = "retorna"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should validate return function keyword`() {
        val keyword = ":"
        assertTrue(Keywords.isKeyword(keyword))
    }

    @Test
    fun `should fail with invalid keywords`() {
        val fakeKeywords = listOf("$", "[", "]", "@", "", " ", ".", "_")
        fakeKeywords.forEach { assertFalse(Keywords.isKeyword(it)) }
    }

    @Test
    fun `should return types`() {
        val validTypes = listOf("int", "real", "texto", "logico", "nada")
        val types = Keywords.types()
        assertEquals(validTypes.size, types.size)
        assertTrue(validTypes.containsAll(types))
    }
}