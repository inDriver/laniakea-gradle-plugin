package utils

import extensions.findNodeByName
import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.model.MutableNode
import models.Graph
import models.GraphNode
import java.io.File

object GraphVizUtil {

    private const val IMAGE_FILE_NAME = "test"
    private const val IMAGE_WIDTH = 5000
    private const val DRAW_ALL_PATHS = -1

    fun generateGraphImage(pictureName: String, graph: Graph) {
        val graphFile = "$pictureName.png"

        val g: MutableGraph = Factory.mutGraph(graphFile)
            .setDirected(true)
            .use { gr, ctx ->
                graph.nodes
                    .forEach { node ->
                        val parentNode = Factory.mutNode(node.name)
                        node.children.forEach { child ->
                            parentNode.addLink(Factory.mutNode(child))
                        }
                    }
            }
        Graphviz.fromGraph(g)
            .width(5000)
            .render(Format.PNG)
            .toFile(File(graphFile))
    }

    fun generateGraphImage(
        appGraph: Graph,
        rootNodeName: String,
        longestPaths: List<List<GraphNode>> = emptyList(),
        pathNumberToHighlight: Int = DRAW_ALL_PATHS
    ) {
        val mutableGraph = Factory.mutGraph(IMAGE_FILE_NAME)
            .setDirected(true)

        val rootNode = appGraph.findNodeByName(rootNodeName)
        val longestPathNodePairs = convertPathsToPairs(longestPaths, pathNumberToHighlight)
        initGraph(mutableGraph, null, appGraph, rootNode, longestPathNodePairs)

        val graphFile = "${IMAGE_FILE_NAME}.png"
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
        longestPathNodePairs: Set<Pair<String, String>>
    ) {
        val mutNode = Factory.mutNode(node.name)

        if (null == parent) {
            mutableGraph.rootNodes().add(mutNode)
        } else {
            val link = parent.linkTo(mutNode)
            val nodePair = Pair("${parent.name()}", "${mutNode.name()}")
            val linkColor = if (nodePair in longestPathNodePairs) {
                Color.RED
            } else {
                Color.BLACK
            }
            link.add(linkColor)
            parent.links().add(link)
        }

        for (childName in node.children) {
            val nextNode = appGraph.findNodeByName(childName)
            initGraph(mutableGraph, mutNode, appGraph, nextNode, longestPathNodePairs)
        }
    }

    private fun convertPathsToPairs(
        longestPaths: List<List<GraphNode>>,
        pathNumberToHighlight: Int
    ): Set<Pair<String, String>> {
        return when {
            longestPaths.isEmpty() -> emptySet()

            pathNumberToHighlight == DRAW_ALL_PATHS -> {
                longestPaths.map { path -> convertPathToPairs(path) }
                    .flatten()
                    .toSet()
            }

            pathNumberToHighlight in 0..longestPaths.lastIndex -> {
                val path = longestPaths[pathNumberToHighlight]
                convertPathToPairs(path)
            }

            else -> emptySet()
        }
    }

    private fun convertPathToPairs(path: List<GraphNode>): Set<Pair<String, String>> {
        val pairs = mutableListOf<Pair<String, String>>()
        path.forEachIndexed { index, node ->
            if (index < path.lastIndex) {
                val nextNode = path[index + 1]
                val pair = Pair(node.name, nextNode.name)
                pairs.add(pair)
            }
        }
        return pairs.toSet()
    }
}
