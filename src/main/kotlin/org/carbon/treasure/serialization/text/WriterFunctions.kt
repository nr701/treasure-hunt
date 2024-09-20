package org.carbon.org.carbon.treasure.serialization.text

import org.carbon.org.carbon.treasure.TreasureMapEngine
import org.carbon.org.carbon.treasure.entities.PositionedTreasureMapEntity
import org.carbon.org.carbon.treasure.entities.TreasureMapEntity

import java.io.File


fun writeToFile(filePath: String, map: TreasureMapEngine) {
    val asString = writeToString(map)
    File(filePath)
        .also { require(!it.exists() || it.canWrite()) { "$filePath does not exists, or is not readable" } }
        .printWriter(Charsets.UTF_8)
        .use { writer -> writer.println(asString) }
}

fun writeToString(map: TreasureMapEngine): String {
    val stringBuilder = StringBuilder()
    map.getPositionedEntities()
        .sortedBy { it.entity.getExportOrder() }
        .map { entity -> entity.toTextDescription() }
        .forEach { stringBuilder.appendLine(it) }
    return stringBuilder.toString()
}

private fun TreasureMapEntity.getExportOrder() : Int =
    when (this) {
        is TreasureMapEntity.MapSize -> 0
        is TreasureMapEntity.Mountain -> 1
        is TreasureMapEntity.Treasure -> 2
        is TreasureMapEntity.Adventurer -> 3
    }

internal fun PositionedTreasureMapEntity.toTextDescription(): String =
    when (this.entity) {
        // {C comme Carte} - {Nb. de case en largeur} - {Nb. de case en hauteur}
        is TreasureMapEntity.MapSize -> "C - ${this.entity.width} - ${this.entity.height}"
        // {M comme Montagne} - {Axe horizontal} - {Axe vertical}
        is TreasureMapEntity.Mountain -> "M - ${this.position.x} - ${this.position.y}"
        // {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}
        is TreasureMapEntity.Treasure -> "T - ${this.position.x} - ${this.position.y} - ${this.entity.quantity}"
        // {A comme Aventurier} - {Nom de l’aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. trésors ramassés}
        is TreasureMapEntity.Adventurer -> "A - ${this.entity.name} - ${this.position.x} - ${this.position.y} - ${this.entity.direction.descriptor} - ${this.entity.collectedTreasure}"
    }

