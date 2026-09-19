# Surefire 3.6.0 + TestNG XML Reproducer

Two independent projects reproduce the Surefire/Failsafe `suiteXmlFiles` change and compare it with Gradle TestNG XML support.

## Project structure

```text
surefire-testng-3.6.0-reproducer/
├── maven/
│   ├── pom.xml                  # Surefire and TestNG checks
│   ├── pom-failsafe.xml         # Failsafe checks
│   ├── pom-legacy-provider.xml  # legacy surefire-testng check
│   ├── mvnw
│   ├── mvnw.cmd
│   └── src/test/
│       ├── java/com/oleynik/qa/reproducer/
│       │   ├── suite/
│       │   │   ├── SelectedTest.java
│       │   │   └── ExcludedTest.java
│       │   └── features/
│       │       ├── ParametersCheck.java
│       │       ├── DependsOnMethodsCheck.java
│       │       ├── DependsOnGroupsCheck.java
│       │       └── GroupsCheck.java
│       └── resources/testng.xml
└── gradle/
    ├── build.gradle
    ├── settings.gradle
    ├── gradlew
    ├── gradlew.bat
    └── src/test/
        ├── java/com/oleynik/qa/reproducer/suite/
        │   ├── SelectedTest.java
        │   └── ExcludedTest.java
        └── resources/testng.xml
```

Requirements: JDK 21. Maven `3.9.12` and Gradle `9.6.0` are provided by the wrappers.

Run Maven commands from `maven/` and Gradle commands from `gradle/`. On Windows, use `mvnw.cmd` and `gradlew.bat` instead of `./mvnw` and `./gradlew`.

## Scenarios

| Scenario                                                      | Command                                                                                             | Expected result                                    |
|---------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|----------------------------------------------------|
| Surefire `3.5.6` + `testng.xml`                               | `./mvnw clean test -Dsurefire.version=3.5.6`                                                        | 1 test; XML is applied                             |
| Surefire `3.6.0-M1` + the same XML                            | `./mvnw clean test -Dsurefire.version=3.6.0-M1`                                                     | 2 tests; XML is ignored                            |
| Surefire `3.6.0` + the same XML                               | `./mvnw clean test -Dsurefire.version=3.6.0`                                                        | 2 tests; XML is ignored                            |
| XML path through Maven property, `3.5.6`                      | `./mvnw clean test -Dsurefire.version=3.5.6 -DsuiteXmlFile=src/test/resources/testng.xml`           | 1 test                                             |
| XML path through Maven property, `3.6.0`                      | `./mvnw clean test -Dsurefire.version=3.6.0 -DsuiteXmlFile=src/test/resources/testng.xml`           | 2 tests                                            |
| XML path through Surefire user property, `3.5.6`              | `./mvnw clean test -Dsurefire.version=3.5.6 -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml` | 1 test                                             |
| XML path through Surefire user property, `3.6.0`              | `./mvnw clean test -Dsurefire.version=3.6.0 -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml` | 2 tests                                            |
| Failsafe `3.5.6` + `testng.xml`                               | `./mvnw -f pom-failsafe.xml clean verify -Dfailsafe.version=3.5.6`                                  | 1 test; XML is applied                             |
| Failsafe `3.6.0-M1` + the same XML                            | `./mvnw -f pom-failsafe.xml clean verify -Dfailsafe.version=3.6.0-M1`                               | 2 tests; XML is ignored                            |
| Failsafe `3.6.0` + the same XML                               | `./mvnw -f pom-failsafe.xml clean verify -Dfailsafe.version=3.6.0`                                  | 2 tests; XML is ignored                            |
| Gradle + the same XML selection                               | `./gradlew clean test`                                                                              | 1 test; XML is applied                             |
| Surefire `3.6.0` without an explicit TestNG Engine dependency | `./mvnw clean test`                                                                                 | 2 TestNG tests; engine is added automatically      |
| Legacy `surefire-testng:3.5.6`                                | `./mvnw -f pom-legacy-provider.xml clean test`                                                      | expected failure: `TestRequest.getSuiteXmlFiles()` |
| Legacy `surefire-testng:3.5.5`                                | `./mvnw -f pom-legacy-provider.xml clean test -Dlegacy.provider.version=3.5.5`                      | expected failure: `TestRequest.getSuiteXmlFiles()` |
| `@Parameters` with a system property                          | `./mvnw clean test -Dtest=ParametersCheck -Dbrowser=chrome`                                         | 1 passing test                                     |
| Required `@Parameters` value is absent                        | `./mvnw clean test -Dtest=ParametersCheck`                                                          | expected missing-parameter failure                 |
| `dependsOnMethods`                                            | `./mvnw clean test -Dtest=DependsOnMethodsCheck`                                                    | expected: 1 failure, 1 skipped test                |
| `dependsOnGroups`                                             | `./mvnw clean test -Dtest=DependsOnGroupsCheck`                                                     | 2 passing tests                                    |
| Include a TestNG group                                        | `./mvnw clean test -Dtest=GroupsCheck -Dgroups=smoke`                                               | only `smoke_test` runs                             |
| Exclude a TestNG group                                        | `./mvnw clean test -Dtest=GroupsCheck -DexcludedGroups=regression`                                  | only `smoke_test` runs                             |

`SelectedTest`, `ExcludedTest`, and `testng.xml` are identical in the Maven and Gradle projects. Both test classes pass; the XML behavior is demonstrated by the number of executed tests.

## Gradle scenario

`gradle/src/test/resources/testng.xml` selects only `SelectedTest`. `ExcludedTest` is present in the same source tree but must not run.

Linux, macOS, or Git Bash:

```bash
cd gradle
./gradlew clean test
```

Windows:

```powershell
cd gradle
./gradlew.bat clean test
```

Expected result:

```text
SelectedTest -> 1 passed test
ExcludedTest -> not executed
BUILD SUCCESSFUL
```

Relevant `build.gradle` configuration:

```groovy
test {
    useTestNG {
        suites 'src/test/resources/testng.xml'
    }
}
```
