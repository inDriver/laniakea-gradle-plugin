package tasks

import extensions.findLongestPaths
import extensions.findRootNodeCandidates
import extensions.getParentToChildrenStructure
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Graph
import models.GraphNode
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File


const val TASK_PROJECT_STATISTICS = "projectStats"
private const val PROJECT_STATS_DIRECTORY = "projectStats"
private const val DEFAULT_STATS_FILE_NAME = "stats"

@Serializable
data class ProjectStatsModel(
    @SerialName("modules_count")
    val modulesCount: Int,
    @SerialName("independent_graphs_count")
    val independentGraphsCount: Int,
    @SerialName("graphs_stats")
    val graphsStats: ArrayList<GraphStatsModel> = arrayListOf(),
    @SerialName("unused_modules")
    val unusedModulesNames: ArrayList<String> = arrayListOf()
)

@Serializable
data class GraphStatsModel(
    @SerialName("root_name")
    val rootNodeName: String,
    @SerialName("modules_count")
    val graphNodesCount: Int,
    @SerialName("longest_path_length")
    val longestPathLength: Int,
    @SerialName("longest_paths_count")
    val longestPathsCount: Int
)

open class ProjectStatisticsTask : DefaultTask() {

    @TaskAction
    fun run() {
        val graph = project.getParentToChildrenStructure(setOf("api", "implementation"))

        val modulesCount = graph.nodes.size
        val rootNodes = graph.findRootNodeCandidates()

        val projectStatsModel = ProjectStatsModel(
            modulesCount = modulesCount,
            independentGraphsCount = rootNodes.size
        )

        rootNodes.forEach { rootNode ->
            if (rootNode.children.isEmpty()) {
                projectStatsModel.unusedModulesNames.add(rootNode.name)
            } else {
                val graphStatsModel = getGraphStats(graph, rootNode)
                projectStatsModel.graphsStats.add(graphStatsModel)
            }
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
        // this one is root node
        var nodesCount = 1

        fun countNodes(graph: Graph, rootNode: GraphNode) {
            rootNode.children.forEach { childName ->
                graph
                    .nodes
                    .first { node ->
                        node.name == childName
                    }
                    .let { node ->
                        nodesCount++
                        countNodes(graph, node)
                    }
            }
        }
        countNodes(graph, rootNode)

        return nodesCount
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
            println("unused modules in project: $unusedModulesNames")
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

        val outputDirectory = File("./${PROJECT_STATS_DIRECTORY}")
        if (!outputDirectory.exists()) {
            val isOutputDirectoryCreated = outputDirectory.mkdir()
            if (!isOutputDirectoryCreated) {
                throw IllegalStateException("Can't create directory for project stats!")
            }
        }

        val fileName = DEFAULT_STATS_FILE_NAME
        val filePath = "${outputDirectory.path}/$fileName-$fileName.json"
        return File(filePath).apply {
            this.outputStream().write(projectStatsJson.encodeToByteArray())
        }

    }
}
