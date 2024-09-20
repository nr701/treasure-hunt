package org.carbon.org.carbon.treasure.entities

import io.klogging.NoCoLogging

/**
 * Moves that can be applied of an entity with a [Position] and a [Direction].
 *
 * TODO: it is debatable whether the text key for Moves (A, F, G) should be here. Serialisation/deserialisation
 * should not be a direct concern of this class.
 */
enum class Move(private val descriptor: Char) {

    FORWARD('A') {
        /**
         * Change position by advancing of 1 cell in the referenced direction.
         */
        override fun apply(position: Position, direction: Direction) =
            Pair(direction.forward(position), direction)
    },

    LEFT('G') {
        /**
         * Change direction by turning once on the left.
         */
        override fun apply(position: Position, direction: Direction) =
            Pair(position, direction.turnLeft())
    },


    RIGHT('D') {
        /**
         * Change direction by turning once on the right.
         */
        override fun apply(position: Position, direction: Direction) =
            Pair(position, direction.turnRight())
    };

    abstract fun apply(position: Position, direction: Direction) : Pair<Position, Direction>

    companion object : NoCoLogging {
        /**
         * Returns the [Move] matching the description, or throw a [IllegalArgumentException] if none matched.
         */
        fun fromDescriptor(descriptor: Char) = entries.firstOrNull { descriptor == it.descriptor } ?: error("Unknown move: $descriptor")
    }
}