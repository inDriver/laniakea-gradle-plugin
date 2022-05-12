package com.indriver.laniakea.tasks

import com.indriver.laniakea.extensions.findLongestPaths
import com.indriver.laniakea.extensions.findRootNodeCandidates
import com.indriver.laniakea.extensions.getParentToChildrenStructure
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.indriver.laniakea.models.Graph
import com.indriver.laniakea.models.GraphNode
import com.indriver.laniakea.models.GraphStats
import com.indriver.laniakea.models.ProjectStats
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import com.indriver.laniakea.utils.PluginConstants
import java.io.File

open class ProjectStatisticsTask : DefaultTask() {

    companion object {
        const val TASK_NAME = "generateProjectModulesStats"
        const val DESCRIPTION = "Generates project statistics"
    }

    @TaskAction
    fun run() {
        println("Running $TASK_NAME")
        val graph = project.getParentToChildrenStructure(PluginConstants.DEFAULT_CONFIGURATIONS)

        val modulesCount = graph.nodes.size
        val rootNodes = graph.findRootNodeCandidates()

        val projectStatsModel = ProjectStats(
            modulesCount = modulesCount,
            independentGraphsCount = rootNodes.size
        )

        rootNodes.forEach { rootNode ->
            val graphStatsModel = getGraphStats(graph, rootNode)
            projectStatsModel.graphsStats.add(graphStatsModel)
        }

        outputResults(projectStatsModel)
    }

    private fun getGraphStats(graph: Graph, rootNode: GraphNode): GraphStats {
        val graphNodesCount = getGraphsNodesCount(graph, rootNode)

        val longestPaths = graph.findLongestPaths(rootNode.name)
        val longestPathLength = longestPaths.first().size
        val longestPathsSize = longestPaths.size

        return GraphStats(
            rootNode.name,
            graphNodesCount,
            longestPathLength,
            longestPathsSize
        )
    }

    private fun getGraphsNodesCount(graph: Graph, rootNode: GraphNode): Int {

        val modulesSet = mutableSetOf<String>()
        modulesSet.add(rootNode.name)

        fun countNodes(graph: Graph, rootNode: GraphNode) {
            rootNode.children.forEach { childName ->
                if (!modulesSet.contains(childName)) {
                    graph
                        .nodes
                        .first { node ->
                            node.name == childName
                        }
                        .let { node ->
                            modulesSet.add(node.name)
                            countNodes(graph, node)
                        }
                }
            }
        }
        countNodes(graph, rootNode)

        return modulesSet.size
    }

    private fun outputResults(projectStats: ProjectStats) {
        printResults(projectStats)
        createOutputFile(projectStats)
    }

    private fun printResults(projectStats: ProjectStats) {
        println("Project statistics:")
        with(projectStats) {
            println("\tproject modules count: $modulesCount")
            println("\tindependent graphs count: $independentGraphsCount")
            println("Independent graph stats:")
            graphsStats.forEach { graphStatsModel ->
                with(graphStatsModel) {
                    println()
                    println("$rootNodeName graph nodes count: $graphNodesCount")
                    println("$rootNodeName graph longest path length: $longestPathLength")
                    println("$rootNodeName graph longest paths count: $longestPathsCount")
                    println()
                }
            }
        }
    }

    private fun createOutputFile(projectStats: ProjectStats): File {
        val projectStatsJson = Json.encodeToString(projectStats)

        val outputDirectory = File(
            System.getProperty("user.dir") +
                    "/${PluginConstants.LANIAKEA_DIRECTORY}/${PluginConstants.GRAPH_MODULES_STATS_DIRECTORY}"
        )
        if (!outputDirectory.exists()) {
            val isOutputDirectoryCreated = outputDirectory.mkdirs()
            if (!isOutputDirectoryCreated) {
                throw IllegalStateException("Can't create directory for project stats!")
            }
        }

        val filePath = "${outputDirectory.path}/${PluginConstants.DEFAULT_STATS_FILE_NAME}.json"
        return File(filePath).apply {
            this.outputStream().write(projectStatsJson.encodeToByteArray())
        }
    }
}
