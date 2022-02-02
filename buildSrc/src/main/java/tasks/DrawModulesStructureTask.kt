package tasks

import extensions.findLongestPaths
import extensions.findRootNodeCandidates
import extensions.getParentToChildrenStructure
import models.Graph
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

    @set:Option(option = "rootModule", description = "root module for critical path")
    @get:Input
    var rootModule: String = ""

    @set:Option(option = "dep", description = "flag to draw all dependencies of modules")
    @get:Input
    var showModulesDependencies: Boolean = false

    @TaskAction
    fun run() {
        println("Running $TASK_DRAW_MODULES_STRUCTURE")
        println("Registered filters: $filtersInput\n")

        val graph = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)
        val filteredNodes = filterNodesIfNeeded(graph.nodes)
        printModulesStructure(filteredNodes)

        val rootNode = getRootNode(graph)
        val longestPaths = if (rootNode != null) {
            val foundLongestPaths = findLongestPaths(graph, rootNode)
            printLongestPaths(foundLongestPaths, rootNode)
            foundLongestPaths
        } else {
            emptyList()
        }

        val filteredGraph = Graph(filteredNodes)
        GraphVizUtil.generateGraphImage(filteredGraph, longestPaths)
    }

    private fun getRootNode(graph: Graph): String? {
        if (rootModule.isNotEmpty()) {
            return rootModule
        }

        val rootNodes = graph.findRootNodeCandidates()
        return when {
            rootNodes.isEmpty() -> {
                println("The project doesn't has a root module")
                null
            }
            rootNodes.size > 1 -> {
                val msg = "The project has a few root modules. " +
                        "You should set a root module: ${rootNodes.map { it.name }}"
                println(msg)
                null
            }
            else -> {
                rootNodes.first().name
            }
        }
    }

    private fun filterNodesIfNeeded(nodesList: List<GraphNode>): List<GraphNode> {
        if (filtersInput.isEmpty()) {
            return nodesList
        }

        val filteredList = nodesList.filter { node ->
            isFilterPassed(node.name)
        }

        return if (showModulesDependencies) {
            filteredList
        } else {
            filteredList.map { node ->
                GraphNode(
                    name = node.name,
                    children = node.children.filter(::isFilterPassed)
                )
            }
        }
    }

    private fun isFilterPassed(name: String) =
        filtersInput.any(name::contains)

    private fun printModulesStructure(graphNodes: List<GraphNode>) {
        println("Modules structure:")
        graphNodes.forEach { node ->
            println(node.name)
            node.children
                .forEach { println("    $it") }
        }
        println()
    }


    private fun findLongestPaths(graph: Graph, rootModule: String): List<List<GraphNode>> {
        return if (!shouldDrawCriticalPath) {
            listOf()
        } else {
            graph.findLongestPaths(rootModule)
        }
    }

    private fun printLongestPaths(paths: List<List<GraphNode>>, currentRootModule: String) {
        if (!shouldDrawCriticalPath) {
            return
        }

        println("Amount of longest paths relative to \"$currentRootModule\" module: ${paths.size}")
        paths.forEachIndexed { index, path ->
            val pathStr = "${index + 1}) ${path.joinToString(separator = " -> ") { it.name }}"
            println(pathStr)
        }
        println()
    }
}
