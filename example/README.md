# Examples

```
./gradlew drawModules --cp
```

```
./gradlew drawModules --cp --dot
```

This command will create .DOT file with the project structure.

```
./gradlew drawModules --filters={feature3,feature2}
```

```
./gradlew validateCriticalPath
```

This command will fail the build because of maximum length restriction.

```
./gradlew generateProjectModulesStats
```

This command will create file with statistics.
