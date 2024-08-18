group = "jiyolla"

repositories {
    mavenCentral()
}

plugins {
    `java-library`
    `java-test-fixtures`
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencyLocking {
    lockAllConfigurations()
}

tasks.register("updateDependencyLocks") {
    group = "dependency management"
    description = "Generates/Update the lock files for all configurations in this module"

    doLast {
        exec {
            logger.lifecycle("Executing command: \"${rootProject.projectDir}/gradlew dependencies --write-locks\" in gradle task")
            workingDir(rootProject.projectDir)
            commandLine("./gradlew", "dependencies", "--write-locks")
        }
    }
}