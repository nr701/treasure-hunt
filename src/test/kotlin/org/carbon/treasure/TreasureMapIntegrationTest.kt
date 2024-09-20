package org.carbon.treasure

import org.carbon.org.carbon.treasure.serialization.text.readFromFile
import org.carbon.org.carbon.treasure.serialization.text.writeToString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File


class TreasureMapIntegrationTest {

    @ParameterizedTest
    @MethodSource("getData")
    fun fullRunTest(inputFileName: String, outputFileName: String) {
        // given
        val engine = readFromFile(TreasureMapIntegrationTest::class.java.getResource(inputFileName)!!.file)
        val expectedEndState = File(TreasureMapIntegrationTest::class.java.getResource(outputFileName)!!.file).readText().trim()

        // when
        engine.fullRun()
        val actualEndState = writeToString(engine).trim()

        // then
        assertEquals(expectedEndState, actualEndState)
    }

    companion object {
        @JvmStatic
        fun getData(): List<Arguments> {
            return listOf(
                Arguments.of("input-1.txt", "output-1.txt")
            )
        }
    }
}