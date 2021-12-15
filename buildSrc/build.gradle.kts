plugins {
    `kotlin-dsl`
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
    implementation("guru.nidi:graphviz-java-all-j2v8:0.18.1") // https://github.com/nidi3/graphviz-java
}
