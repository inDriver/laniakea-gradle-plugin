package extensions

import models.Graph
import models.GraphNode
import models.MostDistantNodesResult
import java.lang.IllegalArgumentException

fun Graph.findNodeByName(name: String): GraphNode {
    val node = nodes.firstOrNull { it.name == name }
    return node ?: throw IllegalArgumentException("Graph has no node with name $name")
}

fun Graph.findLongestPaths(rootNodeName: String): List<List<GraphNode>> {
    if (nodes.isEmpty()) {
        return emptyList()
    }

    val mostDistantNodesResult = findMostDistantNodes(rootNodeName)
    val rootNode = findNodeByName(rootNodeName)
    val currentPath = mutableListOf<GraphNode>()
    val allPaths = mutableSetOf<List<GraphNode>>()

    fun findPath(currentNode: GraphNode, lastNode: GraphNode) {
        currentPath.add(currentNode)
        if (currentNode == lastNode) {
            if (currentPath.size == mostDistantNodesResult.maxDistanceInNodes) {
                allPaths.add(currentPath.toList())
            }
            currentPath.removeLast()
            return
        }

        for (childName in currentNode.children) {
            val childNode = findNodeByName(childName)
            findPath(childNode, lastNode)
        }

        currentPath.removeLast()
    }

    for (node in mostDistantNodesResult.mostDistantNodes) {
        currentPath.clear()
        findPath(rootNode, node)
    }

    return allPaths.toList()
}

fun Graph.findMostDistantNodes(rootNodeName: String): MostDistantNodesResult {
    var maxDistanceInNodes = 0
    val mostDistantNodes = mutableListOf<GraphNode>()

    if (nodes.isEmpty()) {
        return MostDistantNodesResult(maxDistanceInNodes, mostDistantNodes)
    }

    fun findNodes(node: GraphNode, currentDistanceInNodes: Int = 0) {
        val curAmountNodes = currentDistanceInNodes + 1
        if (maxDistanceInNodes < curAmountNodes) {
            maxDistanceInNodes = curAmountNodes
            mostDistantNodes.clear()
            mostDistantNodes.add(node)
        } else if (maxDistanceInNodes == curAmountNodes) {
            mostDistantNodes.add(node)
        }

        for (childName in node.children) {
            val childNode = findNodeByName(childName)
            findNodes(childNode, curAmountNodes)
        }
    }

    val rootNode = findNodeByName(rootNodeName)
    findNodes(rootNode)
    return MostDistantNodesResult(maxDistanceInNodes, mostDistantNodes)
}