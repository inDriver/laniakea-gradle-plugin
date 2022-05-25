# Examples

```
./gradlew drawModules --cp
```

<img src="https://raw.githubusercontent.com/inDriver/laniakea-gradle-plugin/main/example/laniakeaPlugin/images/24.05.22-14.48.53-graph.png" width="521" height="332">

```
./gradlew drawModules --cp --dot
```

This command will create .DOT file with the project structure.

```
./gradlew drawModules --filters={feature3,feature2}
```

<img src="https://raw.githubusercontent.com/inDriver/laniakea-gradle-plugin/main/example/laniakeaPlugin/images/24.05.22-14.52.51-feature3-feature2.png" width="521" height="116">

```
./gradlew validateCriticalPath
```

This command will fail the build because of maximum length restriction.

```
./gradlew generateProjectModulesStats
```

This command will create file with statistics.
