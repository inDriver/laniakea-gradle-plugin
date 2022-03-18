package com.indriver.laniakea.tasks

import com.indriver.laniakea.extensions.findLongestPaths
import com.indriver.laniakea.extensions.findRootNodeCandidates
import com.indriver.laniakea.extensions.getParentToChildrenStructure
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.indriver.laniakea.models.Graph
import com.indriver.laniakea.models.GraphNode
import com.indriver.laniakea.models.GraphStatsModel
import com.indriver.laniakea.models.ProjectStatsModel
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import com.indriver.laniakea.utils.PluginUtils
import java.io.File

const val TASK_PROJECT_MODULES_STATISTICS = "generateProjectModulesStats"
private const val DEFAULT_STATS_FILE_NAME = "stats"

open class ProjectStatisticsTask : DefaultTask() {

    @TaskAction
    fun run() {
        val graph = project.getParentToChildrenStructure(PluginUtils.DEFAULT_CONFIGURATIONS)

        val modulesCount = graph.nodes.size
        val rootNodes = graph.findRootNodeCandidates()

        val projectStatsModel = ProjectStatsModel(
            modulesCount = modulesCount,
            independentGraphsCount = rootNodes.size
        )

        rootNodes.forEach { rootNode ->
            val graphStatsModel = getGraphStats(graph, rootNode)
            projectStatsModel.graphsStats.add(graphStatsModel)
        }

        outputResults(projectStatsModel)
    }

    private fun getGraphStats(graph: Graph, rootNode: GraphNode): GraphStatsModel {
        val graphNodesCount = getGraphsNodesCount(graph, rootNode)

        val longestPaths = graph.findLongestPaths(rootNode.name)
        val longestPathLength = longestPaths.first().size
        val longestPathsSize = longestPaths.size

        return GraphStatsModel(
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

    private fun outputResults(projectStatsModel: ProjectStatsModel) {
        printResults(projectStatsModel)
        createOutputFile(projectStatsModel)
    }

    private fun printResults(projectStatsModel: ProjectStatsModel) {
        println("Project statistics:")

        with(projectStatsModel) {
            println("project modules count: $modulesCount")
            println("independent graphs count: $independentGraphsCount")
            println("independent graph stats:")
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


    private fun createOutputFile(projectStatsModel: ProjectStatsModel): File {
        val projectStatsJson = Json.encodeToString(projectStatsModel)

        val outputDirectory = File(
            System.getProperty("user.dir") +
                    "/${PluginUtils.LANIAKEA_DIRECTORY}/${PluginUtils.GRAPH_MODULES_STATS_DIRECTORY}"
        )
        if (!outputDirectory.exists()) {
            val isOutputDirectoryCreated = outputDirectory.mkdirs()
            if (!isOutputDirectoryCreated) {
                throw IllegalStateException("Can't create directory for project stats!")
            }
        }

        val fileName = DEFAULT_STATS_FILE_NAME
        val filePath = "${outputDirectory.path}/$fileName.json"
        return File(filePath).apply {
            this.outputStream().write(projectStatsJson.encodeToByteArray())
        }

    }
}
