import models.LaniakeaPluginConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import tasks.CheckCriticalPathTask
import tasks.DrawModulesStructureTask
import tasks.TASK_CHECK_CRITICAL_PATH
import tasks.TASK_DRAW_MODULES_STRUCTURE

class LaniakeaPlugin : Plugin<Project> {

    private companion object {
        const val LANIAKEA_PLUGIN_EXTENSION_NAME = "laniakeaPlugin"
    }

    override fun apply(target: Project) {
        val laniakeaPluginConfig = target.extensions
            .create(LANIAKEA_PLUGIN_EXTENSION_NAME, LaniakeaPluginConfig::class.java)

        target.afterEvaluate {
            tasks.register(TASK_DRAW_MODULES_STRUCTURE, DrawModulesStructureTask::class.java) {
                config = laniakeaPluginConfig
            }

            tasks.register(TASK_CHECK_CRITICAL_PATH, CheckCriticalPathTask::class.java) {
                config = laniakeaPluginConfig
            }
        }
    }
}
