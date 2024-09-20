package org.carbon.org.carbon.treasure.entities

/**
 * Combines both a [Position] and a [TreasureMapEntity].
 */
data class PositionedTreasureMapEntity(val position: Position, val entity: TreasureMapEntity)