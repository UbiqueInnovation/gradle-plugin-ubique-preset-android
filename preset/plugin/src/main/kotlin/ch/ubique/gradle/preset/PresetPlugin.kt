package ch.ubique.gradle.preset

import ch.ubique.gradle.preset.config.PresetPluginConfig
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

abstract class PresetPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		val extension = project.extensions.create("ubiquePreset", PresetPluginConfig::class.java, project)

		val androidExtension = project.getAndroidExtension()
		val androidComponentExtension = project.getAndroidComponentsExtension()

		// Apply presets specific to applications
		(androidExtension as? ApplicationExtension)?.applyAppPreset(project)

		// Enable BuildConfig
		androidExtension.buildFeatures.buildConfig = true

		// Exclude library version files on release builds
		androidComponentExtension.onVariants { variant ->
			if (variant.buildType == "release") {
				variant.packaging.resources.excludes.add("META-INF/*.version")
			}
		}

		// Compile with Java 17 compatibility
		androidExtension.compileOptions.apply {
			sourceCompatibility = JavaVersion.VERSION_17
			targetCompatibility = JavaVersion.VERSION_17
		}

		project.tasks.withType(KotlinCompile::class.java) { task ->
			// Annotation targets (Kotlin 2.2+) https://github.com/Kotlin/KEEP/blob/change-defaulting-rule/proposals/annotation-target-in-properties.md
			task.compilerOptions.freeCompilerArgs.add("-Xannotation-default-target=param-property")

			// Let Kotlin target JVM 17
			task.compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
		}

		// Lint settings
		androidExtension.lint.abortOnError = false
	}

	private fun ApplicationExtension.applyAppPreset(project: Project) {
		// Default dimension
		flavorDimensions += "default"

		// Add flavor boolean fields to BuildConfig
		productFlavors.configureEach { flavor ->
			val sanitizedFlavorName = flavor.name.replace("[^a-zA-Z0-9_]", "_")

			// default flavor dimension
			flavor.dimension = "default"

			// default application id suffix
			flavor.applicationIdSuffix = when (flavor.name) {
				"prod", "production" -> null
				else -> ".$sanitizedFlavorName"
			}

			// flavor BuildConfig flag
			val flavorFieldName = "IS_FLAVOR_${sanitizedFlavorName.uppercase()}"
			// true for this flavor ...
			flavor.buildConfigField("boolean", flavorFieldName, "true")
			// ... false for all others
			defaultConfig.buildConfigField("boolean", flavorFieldName, "false")
		}

		// Release build config
		buildTypes.maybeCreate("release").apply {
			isMinifyEnabled = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt", project.layout.buildDirectory), "proguard-rules.pro")
		}

		// R8 full mode check
		if (project.findProperty("android.enableR8.fullMode") !in setOf("true", "false") && project.findProperty("android.enableR8.fullModeAllowed") != "true") {
			throw IllegalArgumentException("R8 full mode is enabled. Disable it with android.enableR8.fullMode=false or allow it by setting android.enableR8.fullModeAllowed=true")
		}
	}

	private fun Project.getAndroidExtension(): CommonExtension {
		return extensions.findByType(ApplicationExtension::class.java)
			?: extensions.findByType(LibraryExtension::class.java)
			?: throw GradleException("Android Gradle Plugin (application or library) has not been applied before")
	}

	private fun Project.getAndroidComponentsExtension(): AndroidComponentsExtension<*, *, *> {
		return extensions.findByType(AndroidComponentsExtension::class.java)
			?: throw GradleException("Android Gradle Plugin has not been applied before")
	}

}
