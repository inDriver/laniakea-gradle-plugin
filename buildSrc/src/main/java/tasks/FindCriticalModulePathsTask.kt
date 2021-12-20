package tasks

import extensions.findLongestPaths
import extensions.findNodeByName
import extensions.getParentToChildrenStructure
import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.model.MutableNode
import models.Graph
import models.GraphNode
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

const val TASK_FIND_CRITICAL_MODULE_PATHS = "findCriticalModulePaths"

open class FindCriticalModulePathsTask : DefaultTask() {

    private companion object {
        const val IMAGE_FILE_NAME = "test"
        const val IMAGE_WIDTH = 5000
        const val DEFAULT_CRITICAL_PATH_INDEX = 0
    }

    @TaskAction
    fun run() {
        println("Running $TASK_FIND_CRITICAL_MODULE_PATHS")

        val graph = project.getParentToChildrenStructure(DEFAULT_CONFIGURATIONS)
        val rootNodeName = ":${project.name}"
        val longestPaths = graph.findLongestPaths(rootNodeName)

        longestPaths.forEachIndexed { index, path ->
            val pathStr = "${index + 1}) ${path.joinToString(separator = " -> ") { it.name }}"
            println(pathStr)
        }

        val selectedNodes = longestPaths[DEFAULT_CRITICAL_PATH_INDEX]
        generateGraphImage(graph, selectedNodes, rootNodeName)
    }

    private fun generateGraphImage(
        appGraph: Graph,
        longestPath: List<GraphNode>,
        rootNodeName: String
    ) {
        val mutableGraph = Factory.mutGraph(IMAGE_FILE_NAME)
            .setDirected(true)

        val rootNode = appGraph.findNodeByName(rootNodeName)
        initGraph(mutableGraph, null, appGraph, rootNode, longestPath)

        val graphFile = "$IMAGE_FILE_NAME.png"
        Graphviz.fromGraph(mutableGraph)
            .width(IMAGE_WIDTH)
            .render(Format.PNG)
            .toFile(File(graphFile))
    }

    private fun initGraph(
        mutableGraph: MutableGraph,
        parent: MutableNode?,
        appGraph: Graph,
        node: GraphNode,
        longestPath: List<GraphNode>
    ) {
        val mutNode = Factory.mutNode(node.name)

        if (null == parent) {
            mutableGraph.rootNodes().add(mutNode)
        } else {
            val link = parent.linkTo(mutNode)
            val parentIndex = longestPath.indexOfFirst {
                it.name == "${parent.name()}"
            }
            val nodeIndex = longestPath.indexOf(node)
            val areIndicesValid = parentIndex != -1 && nodeIndex != -1
            val areAdjacentIndices = parentIndex == nodeIndex - 1
            val linkColor = if (areIndicesValid && areAdjacentIndices) {
                Color.RED
            } else {
                Color.BLACK
            }
            link.add(linkColor)
            parent.links().add(link)
        }

        for (childName in node.children) {
            val nextNode = appGraph.findNodeByName(childName)
            initGraph(mutableGraph, mutNode, appGraph, nextNode, longestPath)
        }
    }
}
