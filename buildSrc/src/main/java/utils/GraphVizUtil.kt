package utils

import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.MutableGraph
import models.Graph
import models.GraphNode
import java.io.File


object GraphVizUtil {

    private const val GRAPH_NAME = "laniakea_graph"
    private const val DRAW_ALL_PATHS = -1

    fun generateGraphImage(
        graph: Graph,
        longestPaths: List<List<GraphNode>> = emptyList(),
        imageFile: File,
        pathNumberToHighlight: Int = DRAW_ALL_PATHS
    ) {
        val graphPairs = convertGraphToPairs(graph)
        val longestPathPairs = convertPathsToPairs(longestPaths, pathNumberToHighlight)

        val mutableGraph = Factory.mutGraph(GRAPH_NAME)
            .setDirected(true)

        graphPairs.forEach { nodePair ->
            val fistNodeName = nodePair.first
            val secondNodeName = nodePair.second

            val firstNode = Factory.mutNode(fistNodeName)
            if (secondNodeName != null) {
                val secondNode = Factory.mutNode(secondNodeName)
                val link = firstNode.linkTo(secondNode)
                val linkColor = if (nodePair in longestPathPairs) Color.RED else Color.BLACK
                link.add(linkColor)
                firstNode.links().add(link)
            }

            mutableGraph.add(firstNode)
        }

        Graphviz.fromGraph(mutableGraph)
            .render(Format.PNG)
            .toFile(imageFile)
    }

    private fun convertGraphToPairs(graph: Graph): Set<Pair<String, String?>> {
        return graph.nodes.map { node ->
            if (node.children.isEmpty()) {
                listOf(Pair(node.name, null))
            } else {
                node.children.map { child ->
                    Pair(node.name, child)
                }
            }
        }
            .flatten()
            .toSet()
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
