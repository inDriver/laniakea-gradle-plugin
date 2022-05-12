package com.indriver.laniakea.tasks

import com.indriver.laniakea.extensions.findLongestPaths
import com.indriver.laniakea.extensions.findRootNodeCandidates
import com.indriver.laniakea.extensions.getParentToChildrenStructure
import com.indriver.laniakea.models.Graph
import com.indriver.laniakea.models.GraphNode
import com.indriver.laniakea.models.LaniakeaPluginConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import com.indriver.laniakea.utils.PluginConstants

open class ValidateCriticalPathTask : DefaultTask() {

    companion object {
        const val TASK_NAME = "validateCriticalPath"
        const val DESCRIPTION = "Validates modules connections critical path"
    }

    @get:Input
    var config = LaniakeaPluginConfig()

    @TaskAction
    fun run() {
        println("Running $TASK_NAME")
        println("Registered max critical path length is: ${config.maxCriticalPathLength}")
        printDivider()
        val graph = project.getParentToChildrenStructure(PluginConstants.DEFAULT_CONFIGURATIONS)
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
