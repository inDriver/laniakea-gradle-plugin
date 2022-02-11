import org.gradle.api.Plugin
import org.gradle.api.Project
import tasks.*

class LaniakeaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.afterEvaluate {
            registerTasks()
        }
    }
}

private fun Project.registerTasks() {
    tasks.register(TASK_RUN_EMPTY, EmptyTask::class.java)
    tasks.register(TASK_DRAW_MODULES_STRUCTURE, DrawModulesStructureTask::class.java)
    tasks.register(TASK_PROJECT_STATISTICS, ProjectStatisticsTask::class.java)
}
