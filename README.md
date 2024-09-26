# Ubique Preset Gradle Plugin

[![Build](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/actions/workflows/build.yml/badge.svg)](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/actions/workflows/build.yml)
[![Gradle Plugin Portal](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/actions/workflows/publish.yml/badge.svg)](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/actions/workflows/publish.yml)

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

## Usage

```kotlin
plugins {
	id("ch.ubique.gradle.preset") version "8.6.0"
}
```

The major and minor version goes in lockstep with the Android Gradle Plugin, 
also see [Releases](https://github.com/UbiqueInnovation/gradle-plugin-ubique-preset-android/releases).
