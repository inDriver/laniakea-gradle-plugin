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
        val parentToChild = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)
        parentToChild.forEach { family ->
            val parent = family.first
            val children = family.second
            println(parent)
            children.forEach { println("    $it") }
        }
    }
}
