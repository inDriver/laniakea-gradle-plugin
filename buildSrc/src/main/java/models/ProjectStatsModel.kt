package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectStatsModel(
    @SerialName("modules_count")
    val modulesCount: Int,
    @SerialName("independent_graphs_count")
    val independentGraphsCount: Int,
    @SerialName("graphs_stats")
    val graphsStats: ArrayList<GraphStatsModel> = arrayListOf(),
    @SerialName("unused_modules")
    val unusedModulesNames: ArrayList<String> = arrayListOf()
)