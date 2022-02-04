package tasks

import extensions.findLongestPaths
import extensions.findRootNodeCandidates
import extensions.getParentToChildrenStructure
import models.Graph
import models.GraphNode
import models.LaniakeaPluginConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import utils.PrintingUtil

const val TASK_CHECK_CRITICAL_PATH = "checkCriticalPath"

open class CheckCriticalPathTask : DefaultTask() {

    @get:Input
    var config = LaniakeaPluginConfig()

    @TaskAction
    fun run() {
        println("Running $TASK_CHECK_CRITICAL_PATH")
        PrintingUtil.printDivider()
        val graph = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)
        val rootNodes = graph.findRootNodeCandidates()
        printPathsInformation(graph, rootNodes)
    }

    private fun printPathsInformation(graph: Graph, rootNodes: Set<GraphNode>) {
        if (rootNodes.isEmpty()) {
            println("The project doesn't have root modules!")
            return
        }

        val rootModuleNames = rootNodes.map { it.name }
        val rootModulesInfo = "Found root modules: " +
                rootModuleNames.joinToString(transform = { name -> "\"$name\""})
        println(rootModulesInfo)

        rootModuleNames.forEach { name ->
            PrintingUtil.printDivider()
            val longestPaths = graph.findLongestPaths(name)
            PrintingUtil.printLongestPathsInformation(name, longestPaths,
                config.maxCriticalPathLength)
        }
    }
}

