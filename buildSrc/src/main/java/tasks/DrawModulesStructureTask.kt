package tasks

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

    @set:Option(option = "dep", description = "flag to draw all dependencies of modules")
    @get:Input
    var showModulesDependencies: Boolean = false

    @TaskAction
    fun run() {
        println("Running $TASK_DRAW_MODULES_STRUCTURE")

        println("Registered filters: $filtersInput")
        println("show modules dependencies: $showModulesDependencies")
        val graph = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)

        val filteredNodes = filterNodesIfNeeded(graph.nodes)
        printModulesStructure(filteredNodes)

        val filteredGraph = Graph(filteredNodes)
        generateGraphImage(filteredGraph)
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
        graphNodes.forEach { node ->
            println(node.name)
            node.children
                .forEach { println("    $it") }
        }
    }

    private fun generateGraphImage(graph: Graph) {
        GraphVizUtil.generateGraphImage("test", graph)
    }
}
