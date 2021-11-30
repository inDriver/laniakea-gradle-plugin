package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

const val TASK_RUN_EMPTY = "runEmptyTask"

open class EmptyTask : DefaultTask() {

    @TaskAction
    fun run() {
        println("Running empty task")
    }
}
