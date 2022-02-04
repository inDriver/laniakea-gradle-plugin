package tasks

import extensions.findLongestPaths
import extensions.getParentToChildrenStructure
import models.LaniakeaPluginConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import utils.PrintingUtil

const val TASK_CHECK_CRITICAL_PATH = "checkCriticalPath"

open class CheckCriticalPathTask : DefaultTask() {

    @get:Input
    var config = LaniakeaPluginConfig()

    private companion object {
        const val DEFAULT_ROOT_MODULE = ":app"
    }

    @TaskAction
    fun run() {
        println("Running $TASK_CHECK_CRITICAL_PATH")
        val rootModule = DEFAULT_ROOT_MODULE
        val graph = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)
        val longestPaths = graph.findLongestPaths(rootModule)
        PrintingUtil.printLongestPathsInformation(rootModule, longestPaths,
            config.maxCriticalPathLength)
    }
}

