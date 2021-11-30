package tasks

import extensions.getParentToChildrenStructure
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

// Default modules connections
val DEFAULT_CONFIGURATIONS = setOf("api", "implementation")
const val TASK_DRAW_MODULES_STRUCTURE = "drawModules"

open class DrawModulesStructureTask : DefaultTask() {

    @TaskAction
    fun run() {
        println("Running $TASK_DRAW_MODULES_STRUCTURE")

        printModulesStructure()
    }

    private fun printModulesStructure() {
        val graph = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)
        graph.nodes.forEach { node ->
            println(node.name)
            node.children
                .forEach { println("    $it") }
        }
    }
}
