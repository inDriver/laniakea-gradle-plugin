import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

const val EMPTY_TASK_NAME = "runEmptyTask"

open class EmptyTask : DefaultTask() {

    @TaskAction
    fun run() {
        println("Running empty task")
    }
}
