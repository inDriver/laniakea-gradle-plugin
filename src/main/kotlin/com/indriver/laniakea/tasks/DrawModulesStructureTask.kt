package com.indriver.laniakea.tasks

import com.indriver.laniakea.extensions.calculateHeight
import com.indriver.laniakea.extensions.findLongestPaths
import com.indriver.laniakea.extensions.findRootNodeCandidates
import com.indriver.laniakea.extensions.getParentToChildrenStructure
import com.indriver.laniakea.models.Graph
import com.indriver.laniakea.models.GraphNode
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import com.indriver.laniakea.utils.GraphVizUtil
import com.indriver.laniakea.utils.FileUtil
import com.indriver.laniakea.utils.PluginConstants
import java.io.File

open class DrawModulesStructureTask : DefaultTask() {

    companion object {
        const val TASK_NAME = "drawModules"
        const val DESCRIPTION = "Draws or creates file with project structure"
    }

    @set:Option(option = "filters", description = "input filters for structure filtering")
    @get:Input
    var filtersInput: List<String> = listOf()

    @set:Option(option = "cp", description = "flag to draw critical path")
    @get:Input
    var shouldDrawCriticalPath: Boolean = false

    @set:Option(option = "rootModule", description = "root module for critical path")
    @get:Input
    var rootModule: String = ""

    @set:Option(option = "dep", description = "flag to draw all dependencies of modules")
    @get:Input
    var showModulesDependencies: Boolean = false

    @set:Option(option = "dot", description = "use this flag is you want to create dot file instead of png")
    @get:Input
    var shouldUseDotFormat: Boolean = false

    @TaskAction
    fun run() {
        println("Running $TASK_NAME")
        println("Registered filters: $filtersInput\n")

        val graph = project.getParentToChildrenStructure(PluginConstants.DEFAULT_CONFIGURATIONS)
        val filteredNodes = filterNodesIfNeeded(graph.nodes)

        val filteredGraph = Graph(filteredNodes)
        val fileType = getImageFileType()
        val imageFile = FileUtil.createImageFile(filtersInput, fileType)
        val longestPathsToDraw = if (shouldDrawCriticalPath)  {
            val rootNode = getRootNode(graph)
            graph.findLongestPaths(rootNode)
        } else {
            emptyList()
        }

        GraphVizUtil.generateGraphImage(filteredGraph, longestPathsToDraw, imageFile, fileType)
        printImageFilePath(imageFile)
    }

    private fun getRootNode(graph: Graph): String? {
        if (rootModule.isNotEmpty()) {
            return rootModule
        }

        val rootNodes = graph.findRootNodeCandidates()
        return when {
            rootNodes.isEmpty() -> {
                println("The project doesn't has a root module")
                null
            }
            rootNodes.size > 1 -> {
                chooseRootNode(graph, rootNodes)
            }
            else -> {
                rootNodes.first().name
            }
        }
    }

    private fun chooseRootNode(graph: Graph, nodes: Set<GraphNode>): String {
        val nodeName = nodes.map { it.name }
            .associateWith { graph.calculateHeight(it) }
            .maxByOrNull { it.value }

        return requireNotNull(nodeName).key
    }

    private fun filterNodesIfNeeded(nodesList: List<GraphNode>): List<GraphNode> {
        if (filtersInput.isEmpty()) {
            return nodesList
        }

        val filteredList = nodesList.filter { node ->
            isFilterPassed(node.name)
        }

        return if (showModulesDependencies) {
            filteredList
        } else {
            filteredList.map { node ->
                GraphNode(
                    name = node.name,
                    children = node.children.filter(::isFilterPassed)
                )
            }
        }
    }

    private fun isFilterPassed(name: String) =
        filtersInput.any(name::contains)

    private fun getImageFileType(): FileUtil.FileType {
        return if (shouldUseDotFormat) {
            FileUtil.FileType.DOT
        } else {
            FileUtil.FileType.PNG
        }
    }

    private fun printModulesStructure(graphNodes: List<GraphNode>) {
        println("Modules structure:")
        graphNodes.forEach { node ->
            println(node.name)
            node.children
                .forEach { println("    $it") }
        }
        println()
    }

    private fun printImageFilePath(file: File) {
        println("Image file path:")
        println(file.absolutePath)
    }
}
