package tasks

import extensions.findLongestPaths
import extensions.getParentToChildrenStructure
import models.GraphNode
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import utils.GraphVizUtil

// Default modules connections
val DEFAULT_CONFIGURATIONS = setOf("api", "implementation")
const val TASK_DRAW_MODULES_STRUCTURE = "drawModules"

open class DrawModulesStructureTask : DefaultTask() {

    @set:Option(option = "filters", description = "input filters for structure filtering")
    @get:Input
    var filtersInput: List<String> = listOf()

    @set:Option(option = "cp", description = "flag to draw critical path")
    @get:Input
    var shouldDrawCriticalPath: Boolean = false

    @TaskAction
    fun run() {
        println("Running $TASK_DRAW_MODULES_STRUCTURE")

        println("Registered filters: $filtersInput\n")
        val graph = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)

        val rootNodeName = ":${project.name}"
        val longestPath = if (shouldDrawCriticalPath) {
            val longestPaths = graph.findLongestPaths(rootNodeName)
            printLongestPaths(longestPaths)
            longestPaths
        } else {
            emptyList()
        }
        GraphVizUtil.generateGraphImage(graph, rootNodeName, longestPath)

        val filteredNodes = filterNodesIfNeeded(graph.nodes)
        printModulesStructure(filteredNodes)
    }

    private fun filterNodesIfNeeded(nodesList: List<GraphNode>): List<GraphNode> =
        if (filtersInput.isEmpty()) {
            nodesList
        } else {
            nodesList.filter { node ->
                filtersInput.any { filter -> node.name.contains(filter) }
            }
        }

    private fun printModulesStructure(graphNodes: List<GraphNode>) {
        println("Modules structure:")
        graphNodes.forEach { node ->
            println(node.name)
            node.children
                .forEach { println("    $it") }
        }
    }

    private fun printLongestPaths(paths: List<List<GraphNode>>) {
        println("Longest paths: ${paths.size}")
        paths.forEachIndexed { index, path ->
                val pathStr = "${index + 1}) ${path.joinToString(separator = " -> ") { it.name }}"
                println(pathStr)
        }
        println()
    }
}
