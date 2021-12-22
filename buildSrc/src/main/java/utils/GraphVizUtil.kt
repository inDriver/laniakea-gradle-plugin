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
        longestPath: List<GraphNode> = emptyList()
    ) {
        val mutableGraph = Factory.mutGraph(IMAGE_FILE_NAME)
            .setDirected(true)

        val rootNode = appGraph.findNodeByName(rootNodeName)
        initGraph(mutableGraph, null, appGraph, rootNode, longestPath)

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
