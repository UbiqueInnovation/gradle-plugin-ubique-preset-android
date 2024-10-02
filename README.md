# Ubique Preset Gradle Plugin

[![Build](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/actions/workflows/build.yml/badge.svg)](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/actions/workflows/build.yml)
[![Release](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/actions/workflows/publish.yml/badge.svg)](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/ch.ubique.gradle/preset.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22ch.ubique.gradle%22%20AND%20a:%22preset%22)

This gradle plugin applies a preset of configurations we use at Ubique to an Android project.

## Preset configuration

The following configurations are applied by this plugin:

* A `default` flavor dimension is created
* The `applicationIsSuffix` of each flavor (except `prod`) is set to `.{flavor}`
* Enable the generation of the `BuildConfig` class
* Adds a boolean flag `IS_FLAVOR_<FLAVOR_NAME>` to the `BuildConfig` for each flavor
* Configures the `release` buildType to enable ProGuard
* Ensures that R8 full mode is either disabled or explicitly enabled
* Excludes `META-INF/*.version` packaging resources from `release` builds
* Sets the `sourceCompatibility` and `targetCompatibility` to Java 17
* Sets the Kotlin `jvmTarget` to Java 17
* Sets the `isAbortOnError` flag of the `lintOptions` to false

In addition to automatically applying a preset configuration, this plugin also provides a few utility functions for usage in build
scripts:

* Methods to read a property from either the project properties or a `local.properties` file in the module or project root
    * `readProperty(propertyName)` for nullable properties
    * `readPropertyWithDefault(propertyName, defaultValue)` for non-nullable properties

## Usage

```kotlin
plugins {
	id("ch.ubique.gradle.preset") version "8.7.0"
}
```

The major and minor version goes in lockstep with the Android Gradle Plugin,
also see [Releases](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/releases).

## Deployment

Create a [Release](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/releases), 
setting the Tag to the desired version prefixed with a `v`.

Each release on Github will be deployed to Maven Central.
