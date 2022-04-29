package com.indriver.laniakea.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GraphStats(
    @SerialName("root_name")
    val rootNodeName: String,
    @SerialName("modules_count")
    val graphNodesCount: Int,
    @SerialName("longest_path_length")
    val longestPathLength: Int,
    @SerialName("longest_paths_count")
    val longestPathsCount: Int
)