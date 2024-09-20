package org.carbon

import io.klogging.config.DEFAULT_CONSOLE
import io.klogging.config.loggingConfiguration
import org.carbon.org.carbon.treasure.serialization.text.readFromFile
import org.carbon.org.carbon.treasure.serialization.text.writeToFile


/**
 * A main entry point for manual test purpose.
 *
 * Expect two filePaths as argument : the first is the input file, the second the output file.
 */
fun main(args : Array<String>) {
    loggingConfiguration { DEFAULT_CONSOLE() }
    require(args.size == 2) { "Expected exactly two filenames as argument. Got ${args.size}." }

    val treasureMap = readFromFile(args[0])

    treasureMap.fullRun()

    writeToFile(args[1], treasureMap)
}