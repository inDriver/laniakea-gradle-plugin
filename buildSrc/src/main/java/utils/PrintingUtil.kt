package utils

import models.GraphNode

object PrintingUtil {

    fun printLongestPathsInformation(
        rootModule: String,
        paths: List<List<GraphNode>>,
        maxCriticalPathLength: Int?
    ) {
        println("\nAmount of longest paths relative to \"$rootModule\" module: ${paths.size}")
        paths.forEachIndexed { index, path ->
            val pathStr = "${index + 1}) ${path.joinToString(separator = " -> ") { it.name }}"
            println(pathStr)
        }

        val currentCriticalPathLength = paths.first().size - 1
        println("\nThe length of the longest path relative to \"$rootModule\" module:" +
                " $currentCriticalPathLength")

        if (maxCriticalPathLength != null) {
            if (currentCriticalPathLength >= maxCriticalPathLength) {
                println("Warning! The length of the longest path is more than the threshold " +
                        "value $maxCriticalPathLength!")
            }
        }
    }
}
