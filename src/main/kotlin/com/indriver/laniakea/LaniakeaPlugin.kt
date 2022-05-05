package com.indriver.laniakea

import com.indriver.laniakea.models.LaniakeaPluginConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.indriver.laniakea.tasks.DrawModulesStructureTask
import com.indriver.laniakea.tasks.ProjectStatisticsTask
import com.indriver.laniakea.tasks.ValidateCriticalPathTask
import com.indriver.laniakea.tasks.TASK_DRAW_MODULES_STRUCTURE
import com.indriver.laniakea.tasks.TASK_PROJECT_MODULES_STATISTICS
import com.indriver.laniakea.tasks.TASK_VALIDATE_CRITICAL_PATH
import com.indriver.laniakea.utils.PluginConstants.PLUGIN_GROUP

class LaniakeaPlugin : Plugin<Project> {

    private companion object {
        const val LANIAKEA_PLUGIN_EXTENSION_NAME = "laniakeaPlugin"
    }

    override fun apply(target: Project) {
        val laniakeaPluginConfig = target.extensions
            .create(LANIAKEA_PLUGIN_EXTENSION_NAME, LaniakeaPluginConfig::class.java)

        target.afterEvaluate { project ->
            project.tasks.register(TASK_DRAW_MODULES_STRUCTURE, DrawModulesStructureTask::class.java) {
                it.group = PLUGIN_GROUP
            }

            project.tasks.register(TASK_VALIDATE_CRITICAL_PATH, ValidateCriticalPathTask::class.java) {
                it.group = PLUGIN_GROUP
                it.config = laniakeaPluginConfig
            }
            project.tasks.register(TASK_PROJECT_MODULES_STATISTICS, ProjectStatisticsTask::class.java) {
                it.group = PLUGIN_GROUP
            }
        }
    }
}
