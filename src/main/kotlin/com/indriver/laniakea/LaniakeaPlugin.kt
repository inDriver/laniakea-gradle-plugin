package com.indriver.laniakea

import com.indriver.laniakea.models.LaniakeaPluginConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.indriver.laniakea.tasks.DrawModulesStructureTask
import com.indriver.laniakea.tasks.ProjectStatisticsTask
import com.indriver.laniakea.tasks.ValidateCriticalPathTask
import com.indriver.laniakea.utils.PluginConstants.PLUGIN_GROUP

class LaniakeaPlugin : Plugin<Project> {

    private companion object {
        const val LANIAKEA_PLUGIN_EXTENSION_NAME = "laniakeaPlugin"
    }

    override fun apply(target: Project) {
        val laniakeaPluginConfig = target.extensions
            .create(LANIAKEA_PLUGIN_EXTENSION_NAME, LaniakeaPluginConfig::class.java)

        target.afterEvaluate { project ->
            project.tasks.register(
                DrawModulesStructureTask.TASK_NAME,
                DrawModulesStructureTask::class.java
            ) {
                it.group = PLUGIN_GROUP
                it.description = DrawModulesStructureTask.DESCRIPTION
            }
            project.tasks.register(
                ValidateCriticalPathTask.TASK_NAME,
                ValidateCriticalPathTask::class.java
            ) {
                it.group = PLUGIN_GROUP
                it.description = ValidateCriticalPathTask.DESCRIPTION
                it.config = laniakeaPluginConfig
            }
            project.tasks.register(
                ProjectStatisticsTask.TASK_NAME,
                ProjectStatisticsTask::class.java
            ) {
                it.group = PLUGIN_GROUP
                it.description = ProjectStatisticsTask.DESCRIPTION
            }
        }
    }
}
