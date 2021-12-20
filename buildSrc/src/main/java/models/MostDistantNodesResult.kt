package models

data class MostDistantNodesResult(
    val maxDistanceInNodes: Int,
    val mostDistantNodes: List<GraphNode>
)
