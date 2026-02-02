pluginManagement {
	repositories {
		google()
		gradlePluginPortal()
		mavenCentral()
	}
}

plugins {
}

dependencyResolutionManagement {
	repositories {
		google()
		mavenCentral()
	}
}

rootProject.name = "gradle-plugin-preset-android"

include("exampleApp")
include("exampleLib")
includeBuild("preset")
