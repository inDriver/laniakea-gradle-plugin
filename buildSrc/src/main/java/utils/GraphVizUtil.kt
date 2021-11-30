package utils

import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.MutableGraph
import models.Graph
import java.io.File

object GraphVizUtil {

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
}
