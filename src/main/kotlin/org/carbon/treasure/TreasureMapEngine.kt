package org.carbon.org.carbon.treasure


import io.klogging.NoCoLogging
import org.carbon.org.carbon.treasure.entities.Position
import org.carbon.org.carbon.treasure.entities.PositionedTreasureMapEntity
import org.carbon.org.carbon.treasure.entities.TreasureMapEntity

/**
 * The central class of the treasure hunt lib.
 *
 * It instantiates from a map data (the list of its entities), run the turns as described by Adventurers, and maintains
 * the map state.
 *
 * Basically, its usage consists in:
 *   - creating an instance from a map (via the [fromEntities] function)
 *   - running moves of adventurers (via the [fullRun] method)
 *   - retrieving the final state of the entities (via the [getPositionedEntities] method)
 *
 * It is also possible to run move turn by turn via calls to [runNextTurn]
 *
 * This class is not thread safe.
 */
class TreasureMapEngine private constructor(
    private val map: Array<Array<TreasureMapEntity?>>,
    private val adventurers: Array<Pair<Position, TreasureMapEntity.Adventurer>>
) {
    /**
     * Returns whether there is at least one adventurer with at least a move to run.
     */
    fun hasMoreTurnsToRun() = adventurers.firstOrNull { it.component2().hasMoreMoves() } != null

    /**
     * Runs the next move for each of the adventurers on the map.
     * If none has more turn to run, does nothing.
     */
    fun runNextTurn() {
        adventurers.forEachIndexed { index, (position, adventurer) ->
            if (adventurer.hasMoreMoves()) {
                val nextMove = adventurer.nextMove()
                val (newPosition, newDirection) = nextMove.apply(position, adventurer.direction)
                logger.info( "Moving ${adventurer.name} from ${position}, direction ${adventurer.direction} to ${newPosition}, direction ${newDirection}" )
                if (isMoveValid(adventurer, newPosition)) {
                    val movedAdventurerWithTreasure = if (newPosition != position) {
                        map[position.x][position.y] = adventurer.treasure?.copy()
                        adventurer
                            .takeTreasureIfAny(map[newPosition.x][newPosition.y])
                            .copy(direction = newDirection)
                    } else {
                        adventurer.copy(direction = newDirection)
                    }

                    map[newPosition.x][newPosition.y] = movedAdventurerWithTreasure
                    adventurers[index] = Pair(newPosition, movedAdventurerWithTreasure)
                } else {
                    logger.warn("Invalid move.")
                }
            }
        }
    }

    /**
     * Runs all moves for all adventurers.
     */
    fun fullRun() {
        while (hasMoreTurnsToRun()) runNextTurn()
    }

    /**
     * Export the state of the map : a list of entities present on the map.
     */
    fun getPositionedEntities() : List<PositionedTreasureMapEntity> {
        logger.info("Exporting entities.")
        val export = (map.mapIndexed { x, subArray ->
            subArray.mapIndexed { y, entity ->
                if (entity != null) {
                    PositionedTreasureMapEntity(
                        Position(x, y),
                        entity
                    )
                } else null
            }
        }
        +
        map.mapIndexed { x, subArray ->
            subArray.mapIndexed { y, entity ->
                if (entity is TreasureMapEntity.Adventurer && entity.treasure != null) {
                    PositionedTreasureMapEntity(
                        Position(x, y),
                        entity.treasure
                    )
                } else null
            }
        }).flatten()
        .filterNotNull()
        logger.info("${export.size} entities found")
        return export
    }

    /**
     * A move is valid for an adventurer if it does not go out of the map, an does not put it on
     * a position already occupied by a mountain or another adventurer.
     */
    private fun isMoveValid(adventurer: TreasureMapEntity.Adventurer, newPosition: Position): Boolean {
        if (newPosition.x in map.indices && newPosition.y in map[0].indices
        ) {
            val entityOnPosition : TreasureMapEntity? = map[newPosition.x][newPosition.y]
            return entityOnPosition == null
                    || entityOnPosition is TreasureMapEntity.Treasure
                    || entityOnPosition == adventurer
        } else {
            return false
        }
    }

    companion object : NoCoLogging {
        /**
         * A factory function returning a [TreasureMapEngine] from a list of [PositionedTreasureMapEntity]
         */
        fun fromEntities(positionedEntities: List<PositionedTreasureMapEntity>) : TreasureMapEngine {
            val map = positionedEntities
                .map { it.entity }
                .filterIsInstance<TreasureMapEntity.MapSize>()
                .also { require(it.size == 1) { "Expected 1 map size descriptor, but got ${it.size}" } }
                .first()
                .let { desc -> Array<Array<TreasureMapEntity?>>(desc.width) { Array(desc.height) { null } } }
            positionedEntities
                .filter { it.entity !is TreasureMapEntity.MapSize }
                .forEach { positionedEntity ->
                    map[positionedEntity.position.x][positionedEntity.position.y] = positionedEntity.entity
                }
            val adventurers = positionedEntities
                .filter { it.entity is TreasureMapEntity.Adventurer }
                .map { Pair(it.position, it.entity as TreasureMapEntity.Adventurer) }
                .toTypedArray()
            return TreasureMapEngine(map, adventurers)
        }
    }
}