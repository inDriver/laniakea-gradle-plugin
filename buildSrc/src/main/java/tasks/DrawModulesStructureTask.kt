package tasks

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

    @TaskAction
    fun run() {
        println("Running $TASK_DRAW_MODULES_STRUCTURE")

        println("Registered filters: $filtersInput")
        val graph = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)
        val filteredNodes = filterNodesIfNeeded(graph.nodes)

        printModulesStructure(filteredNodes)
    }

    private fun filterNodesIfNeeded(nodesList: List<GraphNode>): List<GraphNode> {
        return if (filtersInput.isEmpty()) {
            nodesList
        } else {
            nodesList.filter { node ->
                filtersInput.any { filter -> node.name == filter }
            }
        }
    }

    private fun printModulesStructure(graphNodes: List<GraphNode>) {
        graphNodes.forEach { node ->
            println(node.name)
            node.children
                .forEach { println("    $it") }
        }
    }

    private fun generateGraphImage() {
        val graph = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)
        GraphVizUtil.generateGraphImage("test", graph)
    }
}
