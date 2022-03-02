package com.indriver.laniakea

import org.gradle.api.Plugin
import org.gradle.api.Project

class LaniakeaPlugin : Plugin<Project> {

    private companion object {
        const val LANIAKEA_PLUGIN_EXTENSION_NAME = "laniakeaPlugin"
    }

    override fun apply(target: Project) {
    }
}
