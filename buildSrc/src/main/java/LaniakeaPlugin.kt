import org.gradle.api.Plugin
import org.gradle.api.Project

class LaniakeaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.afterEvaluate {
            registerTasks()
        }
    }
}

private fun Project.registerTasks() {
        tasks.register(EMPTY_TASK_NAME, EmptyTask::class.java)
}
