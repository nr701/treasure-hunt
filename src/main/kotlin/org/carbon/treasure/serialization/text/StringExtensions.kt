package org.carbon.org.carbon.treasure.serialization.text

import org.carbon.org.carbon.treasure.entities.Direction
import org.carbon.org.carbon.treasure.entities.Move


/** Returns whether this String is a valid description for (i.e. can be converted to) a [Direction]. */
fun String.isValidDirectionDescriptor() = this in listOf("N", "S", "E", "O")

/** Returns whether this String is a valid description for (i.e. can be converted to) an [Int]. */
fun String.isValidInt() = this.toIntOrNull() != null

/**
 * Returns the [Direction] for this String description.
 * Can throw an exception if none matches, [isValidDirectionDescriptor] should be checked before.
 */
fun String.toDirection() = Direction.fromDescriptor(this)

/** Returns whether this String is a valid description for (i.e. can be converted to) a list of [Move]s.  */
fun String.isValidMoveSequence() = this.firstOrNull { it !in listOf('A', 'G', 'D') } == null

/**
 * Returns an iterator of [Move] for this String description.
 * Can throw an exception if the description is invalid, [isValidMoveSequence] should be checked before.
 */
fun String.toMoveIterator() = this.asSequence().map { Move.fromDescriptor(it) }.iterator()