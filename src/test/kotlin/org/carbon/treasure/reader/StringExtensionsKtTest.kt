package org.carbon.treasure.reader

import org.carbon.org.carbon.treasure.serialization.text.isValidInt
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class StringExtensionsKtTest {

    @ParameterizedTest
    @MethodSource("getDataForIntValidation")
    fun testStringIsInt(s: String, expected: Boolean) {
        assertEquals(s.isValidInt(), expected)
    }

    companion object {
        @JvmStatic
        fun getDataForIntValidation(): List<Arguments> {
            return listOf(
                Arguments.of("1", true),
                Arguments.of("14", true),
                Arguments.of("T", false),
                Arguments.of("two", false)
            )
        }
    }
}