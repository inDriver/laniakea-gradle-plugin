package extensions

import models.Graph
import models.GraphNode
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency

/**
 * Collects modules into a structure with parent and children modules.
 *
 * @return - pair with parent and their children.
 */
fun Project.getParentToChildrenStructure(
    configurationsToLook: Set<String>
): Graph {
    val parentToChild = hashMapOf<String, ArrayList<String>>()
    rootProject.subprojects
        .flatMap { subProject ->
            subProject.configurations
                .filter { subProjectConfiguration ->
                    configurationsToLook.contains(subProjectConfiguration.name)
                }
                .flatMap { configuration ->

                    // Add parent with empty children
                    if (parentToChild[subProject.getClearDisplayName()] == null) {
                        parentToChild[subProject.getClearDisplayName()] = arrayListOf()
                    }

                    configuration.dependencies
                        .filterIsInstance(DefaultProjectDependency::class.java)
                        .map { it.dependencyProject }
                }
                .map { subProjectDependency ->
                    // Add child
                    val child = subProjectDependency.getClearDisplayName()
                    parentToChild[subProject.getClearDisplayName()]?.add(child)
                }
        }

    val graphNodes = parentToChild.toList()
        .map { GraphNode(it.first, it.second) }
    return Graph(graphNodes)
}

/**
 * Return clear display name.
 * project ':app' -> :app
 */
fun Project.getClearDisplayName(): String {
    return displayName.replace("project", "")
        .replace("'", "")
        .trim()
}
