package tasks

import extensions.findLongestPaths
import extensions.findRootNodeCandidates
import extensions.getParentToChildrenStructure
import models.Graph
import models.GraphNode
import models.LaniakeaPluginConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

const val TASK_VALIDATE_CRITICAL_PATH = "validateCriticalPath"

open class ValidateCriticalPathTask : DefaultTask() {

    @get:Input
    var config = LaniakeaPluginConfig()

    @TaskAction
    fun run() {
        println("Running $TASK_VALIDATE_CRITICAL_PATH")
        printDivider()
        val graph = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)
        val rootNodes = graph.findRootNodeCandidates()
        printPathsInfo(graph, rootNodes)
    }

    private fun printPathsInfo(graph: Graph, rootNodes: Set<GraphNode>) {
        if (rootNodes.isEmpty()) {
            println("The project doesn't have root modules!")
            return
        }

        val rootModuleNames = rootNodes.map { it.name }
        val rootModulesInfo = "Found root modules: " +
                rootModuleNames.joinToString(transform = { name -> "\"$name\""})
        println(rootModulesInfo)

        rootModuleNames.forEach { name ->
            printDivider()
            val longestPaths = graph.findLongestPaths(name)
            printLongestPathsInfo(name, longestPaths, config.maxCriticalPathLength)
        }
    }

    private fun printLongestPathsInfo(
        rootModule: String,
        paths: List<List<GraphNode>>,
        maxCriticalPathLength: Int?
    ) {
        println("Amount of longest paths relative to \"$rootModule\" module: ${paths.size}")
        paths.forEachIndexed { index, path ->
            val pathStr = "${index + 1}) ${path.joinToString(separator = " -> ") { it.name }}"
            println(pathStr)
        }

        val currentCriticalPathLength = paths.first().size - 1
        println("\nThe length of the longest path relative to \"$rootModule\" module:" +
                " $currentCriticalPathLength")

        if (maxCriticalPathLength != null) {
            if (currentCriticalPathLength >= maxCriticalPathLength) {
                val message = "WARNING! The length of the longest path is more than the threshold " +
                        "value $maxCriticalPathLength!"
                throw IllegalStateException(message)
            }
        }
    }

    private fun printDivider() = println("---------------------------------")
}

