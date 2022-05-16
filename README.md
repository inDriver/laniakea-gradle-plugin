# Laniakea Gradle Plugin ✨

TODO(dv): repo version label

TODO(dv): example: connect and add images

A Gradle Plugin that helps to understand your project modules structure.

What does the Laniakea word mean? This is the galaxy supercluster ✨. [See more info here](https://en.wikipedia.org/wiki/Laniakea_Supercluster)

## Overview

The main idea of this plugin is to help developers understand their project structure. They can easily visualize the whole project or only a small part.
Or even more, developers can visualize module clusters together and compare different approaches, find non-obvious connections, and so on.

### Features

- Project structure visualization with result filtering. No additional tools are required*
- Modules critical paths visualization. Understand how these paths can affect your structure
- Critical path length validation
- Project statistics (modules count, critical paths lengths, etc)
- PNG/DOT file formats for the result

*We use [this Grapviz wrapper](https://github.com/nidi3/graphviz-java). Thanks authors for this tool.

## Installation

## Usage

For a project structure visualization run the following:

```
./gradlew drawModules
```
This command will create an image of all project modules inside `/laniakeaImage` folder.

For this command few options are available:
- `--filters` - will filter project structure. You can filter your structure by one filter or combine them. For examle, `--filters=core` or `--filters={core,group1,group2}`
- `--cp` - will highlight the critical path
- `--dot` - use this option if you want to create .dot file with project structure instead of png image. This option may be useful if image creation doesn't work (it may happen in some cases)

For a critical path validation run the following:

```
./gradlew validateCriticalPath
```

You have to specify `maxCriticalPathLength` param for this command inside the plugin configuration block.
Add this inside your root `build.gradle`:
```
laniakeaPlugin {    
    maxCriticalPathLength = 10
}
```

For project statistics run the following:
```
./gradlew generateProjectModulesStats
```

This command will show you project statistics and create a json file with these statistics. This feature is still under construction, but you can use it for simple statistics.

## Inspiration

Thanks to other tools for inspiration:

- [Module Graph Assert](https://github.com/jraska/modules-graph-assert)
- [module-dependency-graph](https://github.com/savvasdalkitsis/module-dependency-graph)

## License

This plugin is released under the Apache 2.0 license. [See LICENSE](https://github.com/inDriver/laniakea-gradle-plugin/blob/main/LICENSE) for details.

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
