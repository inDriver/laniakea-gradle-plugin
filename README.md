# Laniakea Gradle Plugin ✨

[![](https://jitpack.io/v/inDriver/laniakea-gradle-plugin.svg)](https://jitpack.io/#inDriver/laniakea-gradle-plugin)

A Gradle Plugin that helps to understand your project modules structure.

What does the Laniakea word mean? This is the galaxy supercluster ✨. [See more info here](https://en.wikipedia.org/wiki/Laniakea_Supercluster)

## Overview

The main purpose of this plugin is to help developers understand their project structure. It allows them to easily visualize the whole project, or only a small part of it, depending on their preference. Additionally, developers can use this tool to visualize module clusters together and compare different approaches, locate connections that may not be immediately obvious, and so on.

<img src="https://raw.githubusercontent.com/inDriver/laniakea-gradle-plugin/main/example/laniakeaPlugin/images/24.05.22-14.48.53-graph.png" width="521" height="332">

### Features

- Visualization of project structure with result filtering. No additional tools are required*
- Visualization of modules critical paths – understand how these paths can affect your structure
- Validation of the lengths of critical paths
- Project statistics (module count, lengths of critical paths, etc.)
- Results in .PNG/.DOT file format

*We use [this Grapviz wrapper](https://github.com/nidi3/graphviz-java). Many thanks to the authors for this tool!

## Installation

Add it in your root build.gradle:

```
buildscript {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        // ...
        classpath "com.github.inDriver:laniakea-gradle-plugin:1.0.0"
    }
}

apply plugin: 'com.indriver.laniakea.plugin'
```

## Usage

To visualize project structure, run the following command:

```
./gradlew drawModules
```
This will create an image of all project modules inside the /laniakeaImage folder.

There are a few options available for this command:
- `--filters` - will filter the project structure. You can filter your structure (by modules names) using a single filter or a combination of several filters, for example: `--filters=core` or `--filters={core,group1,group2}`
- `--cp` - will highlight the critical path
- `--dot` - use this option if you want to create a .dot file with the project structure instead of a .png image. This option can be useful if image creation doesn't work (which may happen in some cases)
- `--dep` - will add dependencies for filtered modules, for example: `./gradlew drawModules --filters=features --dep`
- `--rootModule` - you can specify the "root module" to find the critical path. By default, the project's root module is searched automatically

To validate a critical path, run the following command:

```
./gradlew validateCriticalPath
```

You will need to specify the parameter `maxCriticalPathLength` for this command inside the plugin configuration block. 
Add this inside your root `build.gradle`:
```
laniakeaPlugin {    
    maxCriticalPathLength = 10
}
```

For project statistics, run the following command:
```
./gradlew generateProjectModulesStats
```

This will display the project statistics, and create a .json file containing these statistics. This feature is still under construction, but you can use it for simple statistics.

## Future plans

- Build time tracker for modules
  
- Interactive map for modules (with filters, "heat map", etc) 

## Inspiration

Thanks to these tools for inspiration:

- [Module Graph Assert](https://github.com/jraska/modules-graph-assert)
- [module-dependency-graph](https://github.com/savvasdalkitsis/module-dependency-graph)

## License

This plugin has been released under the Apache 2.0 license. [See LICENSE](https://github.com/inDriver/laniakea-gradle-plugin/blob/main/LICENSE) for details.

    Copyright 2022  Suol Innovations Ltd.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
