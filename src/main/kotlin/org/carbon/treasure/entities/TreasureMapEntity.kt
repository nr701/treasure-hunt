package org.carbon.org.carbon.treasure.entities

import io.klogging.NoCoLogging

/**
 * Describes the entities on a treasure hunt map.
 * There is an implementation per entity (mountain, treasure...) plus one for the map itself.
 * The plains are implicit entities (or rather the absence of entity denotes a plain), and do not have an implementation.
 */
sealed interface TreasureMapEntity {

    /**
     * A particular entity containing the map size.
     */
    data class MapSize(val width: Int, val height: Int) : TreasureMapEntity

    /**
     * An entity describing a mountain
     */
    data object Mountain : TreasureMapEntity

    /**
     * An entity describing a treasure.
     */
    data class Treasure(internal val quantity: Int) : TreasureMapEntity

    /**
     * An entity describing an Adventurer.
     */
    data class Adventurer(
        val name: String,
        internal val direction: Direction,
        internal val moves: Iterator<Move>,
        internal val collectedTreasure: Int = 0,
        internal val treasure: Treasure? = null) : TreasureMapEntity
    {
            fun hasMoreMoves() = moves.hasNext()
            fun nextMove() = moves.next()

        /**
         * Returns a copy of this instance, updated with treasure data, if any.
         * To be called with the (maybe null) treasure to capture.
         */
        fun takeTreasureIfAny(treasure: TreasureMapEntity?): Adventurer {
                return if (treasure is Treasure) {
                    logger.info("${this.name} takes treasure $treasure")
                    this.copy(
                        collectedTreasure = if (treasure.quantity > 0) collectedTreasure + 1 else collectedTreasure,
                        treasure = if (treasure.quantity > 0) treasure.copy(quantity = treasure.quantity - 1) else treasure
                    )
                } else {
                    this.copy(treasure = null)
                }
            }
        }

    companion object : NoCoLogging
}


