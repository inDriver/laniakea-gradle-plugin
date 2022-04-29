package com.indriver.laniakea.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectStats(
    @SerialName("modules_count")
    val modulesCount: Int,
    @SerialName("independent_graphs_count")
    val independentGraphsCount: Int,
    @SerialName("graphs_stats")
    val graphsStats: ArrayList<GraphStats> = arrayListOf()
)