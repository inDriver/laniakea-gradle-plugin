plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version "1.6.10"
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("BuildManager") {
            id = "com.indriver.laniakea.plugin"
            implementationClass = "LaniakeaPlugin"
            version = "0.0.1"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation("guru.nidi:graphviz-java-all-j2v8:0.18.1") // https://github.com/nidi3/graphviz-java
}
