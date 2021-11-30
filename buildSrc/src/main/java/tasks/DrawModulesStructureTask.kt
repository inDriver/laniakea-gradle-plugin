package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

const val TASK_DRAW_MODULES_STRUCTURE = "drawModules"

open class DrawModulesStructureTask : DefaultTask() {

    @TaskAction
    fun run() {
        println("Running $TASK_DRAW_MODULES_STRUCTURE")
    }
}
