package org.carbon.treasure.reader

import org.carbon.org.carbon.treasure.entities.*
import org.carbon.org.carbon.treasure.serialization.text.fromTextDescription
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class MapReaderFunctionsTest {

    @ParameterizedTest
    @MethodSource("getData")
    fun testBasicRead(descriptor: String, expectedEntity: PositionedTreasureMapEntity) {
        val actualEntity = fromTextDescription(descriptor)
        assertEquals(expectedEntity, actualEntity)
    }

    @ParameterizedTest
    @MethodSource("getBadData")
    fun testReadFailures(descriptor: String, expectedMessage: String) {
        val exception: Exception = assertThrows {
            fromTextDescription(descriptor)
        }
        assertTrue(exception is IllegalArgumentException)
        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun testBasicReadOnAdventurer() {
        // given
        val descriptor = "A - Lara - 1 - 1 - S - AADADAGGA"
        val expectedAdventurer = TreasureMapEntity.Adventurer("Lara", Direction.SOUTH, listOf(Move.FORWARD, Move.FORWARD, Move.RIGHT, Move.FORWARD, Move.RIGHT, Move.FORWARD, Move.LEFT, Move.LEFT, Move.FORWARD).listIterator())
        val expectedPosition = Position(1, 1)

        // when
        val actualEntity = fromTextDescription(descriptor)

        // then
        assertEquals(expectedPosition, actualEntity.position)
        assertTrue(actualEntity.entity is TreasureMapEntity.Adventurer)
        val actualAdventurer = actualEntity.entity as TreasureMapEntity.Adventurer
        assertEquals(expectedAdventurer.name, actualAdventurer.name)
        assertEquals(expectedAdventurer.direction, actualAdventurer.direction)
        assertEquals(expectedAdventurer.treasure, actualAdventurer.treasure)
        assertEquals(expectedAdventurer.collectedTreasure, actualAdventurer.collectedTreasure)
        assertEquals(expectedAdventurer.moves.asSequence().toList(), actualAdventurer.moves.asSequence().toList())

    }

    companion object {
        @JvmStatic
        fun getData(): List<Arguments> {
            return listOf(
                Arguments.of("C - 1 - 1", PositionedTreasureMapEntity(Position(0, 0), TreasureMapEntity.MapSize(1, 1))),
                Arguments.of("M - 14 - 25", PositionedTreasureMapEntity(Position(14, 25), TreasureMapEntity.Mountain)),
                Arguments.of("T - 5 - 8 - 3", PositionedTreasureMapEntity(Position(5, 8), TreasureMapEntity.Treasure(3)))
                // TODO more cases
            )
        }

        @JvmStatic
        fun getBadData(): List<Arguments> {
            return listOf(
                Arguments.of("Z - 1 - 1", "Unknown entity type Z"),
                Arguments.of("C - 1 - 1 - 1", "Map size descriptor should have 3 elements, but got 4 : C - 1 - 1 - 1"),
                Arguments.of("C - 1 - 1a", "Expected an int for the map height, got 1a."),
                Arguments.of("M - 1 - 1 - A", "Mountain descriptor should have 3 elements, but got 4 : M - 1 - 1 - A"),
                Arguments.of("T - 1 - 1", "Treasure descriptor should have 4 elements, but got 3 : T - 1 - 1"),
                // TODO more cases
            )
        }
    }

}