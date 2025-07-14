# Proyecto base de Serenity BDD

### Ejecución de test por línea de comandos

#### Maven

```json
mvn clean verify -Dcucumber.filter.tags=@login -Denvironment=certification

```

#### Gradle

```json
$ gradlew clean test -Pdriver=chrome
```
