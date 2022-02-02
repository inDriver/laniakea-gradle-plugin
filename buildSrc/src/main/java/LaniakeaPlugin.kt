import models.LaniakeaPluginConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import tasks.DrawModulesStructureTask
import tasks.EmptyTask
import tasks.TASK_DRAW_MODULES_STRUCTURE
import tasks.TASK_RUN_EMPTY

class LaniakeaPlugin : Plugin<Project> {

    private companion object {
        const val LANIAKEA_PLUGIN_EXTENSION_NAME = "laniakeaPlugin"
    }

    override fun apply(target: Project) {
        val laniakeaPluginConfig = target.extensions
            .create(LANIAKEA_PLUGIN_EXTENSION_NAME, LaniakeaPluginConfig::class.java)

        target.afterEvaluate {
            tasks.register(TASK_RUN_EMPTY, EmptyTask::class.java)
            tasks.register(TASK_DRAW_MODULES_STRUCTURE, DrawModulesStructureTask::class.java) {
                config = laniakeaPluginConfig
            }
        }
    }
}
