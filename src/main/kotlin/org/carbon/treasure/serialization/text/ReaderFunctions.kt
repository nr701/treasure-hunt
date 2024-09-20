package org.carbon.org.carbon.treasure.serialization.text

import org.carbon.org.carbon.treasure.TreasureMapEngine
import org.carbon.org.carbon.treasure.entities.*
import org.carbon.org.carbon.treasure.entities.TreasureMapEntity.*

import java.io.File

/**
 * Creates a [TreasureMapEngine] in its initial state from a file description.
 */
fun readFromFile(fileName: String) =
    File(fileName)
        .also { require(it.exists() && it.isFile && it.canRead()) { "$fileName does not exists, or is not readable" } }
        .readLines()
        .run(::readFromDescriptors)

/**
 * Creates a [TreasureMapEngine] in its initial state from a list of entities text descriptions.
 */
fun readFromDescriptors(textDescriptions: List<String>) =
    textDescriptions
        .map { fromTextDescription(it) }
        .let { TreasureMapEngine.fromEntities(it) }

/**
 * Creates a [PositionedTreasureMapEntity] from its text description.
 */
fun fromTextDescription(textDescription: String): PositionedTreasureMapEntity =
    when (textDescription[0]) {
        'C' -> textToMapSize(textDescription)
        'M' -> textToMountain(textDescription)
        'T' -> textToTreasure(textDescription)
        'A' -> textToAdventurer(textDescription)
        else -> throw IllegalArgumentException("Unknown entity type ${textDescription[0]}")
    }

private fun textToMapSize(textDescription: String) =
    textDescription
        .split(" - ")
        .also { require(it.size == 3) { "Map size descriptor should have 3 elements, but got ${it.size} : $textDescription" } }
        .also { require(it[1].isValidInt()) { "Expected an int for the map width, got ${it[1]}." } }
        .also { require(it[2].isValidInt()) { "Expected an int for the map height, got ${it[2]}." } }
        .let {
            PositionedTreasureMapEntity(
                Position(0, 0),
                MapSize(it[1].toInt(), it[2].toInt()))
        }

private fun textToMountain(textDescription: String) =
    textDescription
        .split(" - ")
        .also { require(it.size == 3) { "Mountain descriptor should have 3 elements, but got ${it.size} : $textDescription" } }
        .also { require(it[1].isValidInt()) { "Expected an int for a mountain x location, got ${it[1]}." } }
        .also { require(it[2].isValidInt()) { "Expected an int for a mountain y location, got ${it[2]}." } }
        .let {
            PositionedTreasureMapEntity(
                Position(it[1].toInt(), it[2].toInt()),
                Mountain
            )
        }

private fun textToTreasure(textDescription: String) =
    textDescription
        .split(" - ")
        .also { require(it.size == 4) { "Treasure descriptor should have 4 elements, but got ${it.size} : $textDescription" } }
        .also { require(it[1].isValidInt()) { "Expected an int for a treasure x location, got ${it[1]}." } }
        .also { require(it[2].isValidInt()) { "Expected an int for a treasure y location, got ${it[2]}." } }
        .let {
            PositionedTreasureMapEntity(
                Position(it[1].toInt(), it[2].toInt()),
                Treasure(it[3].toInt()))
        }

private fun textToAdventurer(textDescription: String) =
    textDescription
        .split(" - ")
        .also { require(it.size == 6) { "Adventurer descriptor should have 5 elements, but got ${it.size} : $textDescription" } }
        .also { require(it[1].isNotBlank()) { "Expected the Adventurer to have a non-empty name, got ${it[1]}." } }
        .also { require(it[2].isValidInt()) { "Expected an int for an adventurer x location, got ${it[2]}." } }
        .also { require(it[3].isValidInt()) { "Expected an int for an adventurer y location, got ${it[3]}." } }
        .also { require(it[4].isValidDirectionDescriptor()) { "Expected a valid direction descriptor, got ${it[4]}." } }
        .also { require(it[5].isValidMoveSequence()) { "Expected a valid move sequence, got ${it[5]}." } }
        .let {
            PositionedTreasureMapEntity(
                Position(it[2].toInt(), it[3].toInt()),
                Adventurer(it[1], it[4].toDirection(), it[5].toMoveIterator()))
        }
