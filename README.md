# Laniakea Gradle Plugin ✨

TODO(dv): repo version label

TODO(dv): example: connect and add images

A Gradle Plugin that helps to understand your project modules structure.

What the Laniakea word means? This is the galaxy supercluster ✨. [See more info here](https://en.wikipedia.org/wiki/Laniakea_Supercluster)

## Overview

The main idea of this plugin is to help developers understand their project structure. They can easily visualize whole project or only small part.
Or even more, developers can visualize modules clusters together and compare different approaches, find non-obvius connections and so on.

TODO(dv): "why and for that" block

### Features

- Project structure visualization with result filtering. No additional tools required*
- Modules critical paths visualization. Understand how this paths can affect your structure
- Critical path length validation
- Project statistics (modules count, critical paths lengths, etc)
- PNG/DOT file formats for result 

*We use [this Grapviz wrapper](https://github.com/nidi3/graphviz-java). Thanks authors for this tool.

## Usage

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
