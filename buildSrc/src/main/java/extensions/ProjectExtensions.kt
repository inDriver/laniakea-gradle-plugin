package extensions

import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency

/**
 * Collects modules into a structure with parent and children modules.
 *
 * @return - pair with parent and their children.
 */
fun Project.getParentToChildrenStructure(
    configurationsToLook: Set<String>
): List<Pair<String, ArrayList<String>>> {
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

    return parentToChild.toList()
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
