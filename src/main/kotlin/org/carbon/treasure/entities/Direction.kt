package org.carbon.org.carbon.treasure.entities

/**
 * A cardinal direction.
 *
 * TODO: it is debatable whether the text key for Directions (N, S, W, E) should be here. Serialisation/deserialisation
 * should not be a direct concern of this class.
 *
 */
enum class Direction(val descriptor: String) {
    NORTH("N") {
        override fun turnLeft() = WEST
        override fun turnRight() = EAST
        override fun forward(position : Position) = position.copy(y = position.y - 1)
    },
    SOUTH("S") {
        override fun turnLeft() = EAST
        override fun turnRight() = WEST
        override fun forward(position : Position) = position.copy(y = position.y + 1)
    },
    EAST("E") {
        override fun turnLeft() = NORTH
        override fun turnRight() = SOUTH
        override fun forward(position : Position) = position.copy(x = position.x + 1)
    },
    WEST ("O") {
        override fun turnLeft() = SOUTH
        override fun turnRight() = NORTH
        override fun forward(position : Position) = position.copy(x = position.x - 1)
    };

    /**
     * Return the [Direction] obtained after a left turn.
     */
    abstract fun turnLeft() : Direction
    /**
     * Return the [Direction] obtained after a right turn.
     */
    abstract fun turnRight() : Direction
    /**
     * Return the [Position] obtained advancing of one cell in this direction. This [Position] is not guaranteed to be
     * correct in the context of a map.
     */
    abstract fun forward(position: Position): Position

    companion object {
        /**
         * Returns the [Direction] matching the description, or throw a [IllegalArgumentException] if none matched.
         */
        fun fromDescriptor(descriptor: String) = entries.firstOrNull { descriptor == it.descriptor } ?: error("Unknown direction: $descriptor")
    }
}