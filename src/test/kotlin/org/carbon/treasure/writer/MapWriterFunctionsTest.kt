package org.carbon.treasure.writer

import org.carbon.org.carbon.treasure.entities.Position
import org.carbon.org.carbon.treasure.entities.PositionedTreasureMapEntity
import org.carbon.org.carbon.treasure.entities.TreasureMapEntity
import org.carbon.org.carbon.treasure.serialization.text.toTextDescription
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class MapWriterFunctionsTest {
    @ParameterizedTest
    @MethodSource("getData")
    fun testBasicWrite(entity: PositionedTreasureMapEntity, expectedDescriptor: String) {
        val actualDescriptor = entity.toTextDescription()
        assertEquals(expectedDescriptor, actualDescriptor)
    }

    companion object {
        @JvmStatic
        fun getData(): List<Arguments> {
            return listOf(
                Arguments.of(PositionedTreasureMapEntity(Position(0, 0), TreasureMapEntity.MapSize(1, 1)),"C - 1 - 1"),
                Arguments.of(PositionedTreasureMapEntity(Position(14, 25), TreasureMapEntity.Mountain), "M - 14 - 25"),
                Arguments.of(PositionedTreasureMapEntity(Position(5, 8), TreasureMapEntity.Treasure(3)), "T - 5 - 8 - 3")
                // TODO more cases
            )
        }
    }
}