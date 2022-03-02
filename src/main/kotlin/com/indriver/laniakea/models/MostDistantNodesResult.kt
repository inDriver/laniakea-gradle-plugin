package com.indriver.laniakea.models

data class MostDistantNodesResult(
    val maxDistanceInNodes: Int,
    val mostDistantNodes: List<GraphNode>
)
