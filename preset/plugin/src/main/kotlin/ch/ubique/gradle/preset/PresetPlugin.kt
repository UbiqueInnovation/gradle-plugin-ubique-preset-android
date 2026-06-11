package ch.ubique.gradle.preset

import ch.ubique.gradle.preset.config.PresetPluginConfig
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.cc.base.logger
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

abstract class PresetPlugin : Plugin<Project> {

	private companion object {
		private const val DEFAULT_JDK_VERSION = 21
	}

	override fun apply(project: Project) {
		val extension = project.extensions.create("ubiquePreset", PresetPluginConfig::class.java, project)

		val jdkVersion = project.findProperty("ubique.preset.jdkVersion")?.toString()?.toIntOrNull() ?: run {
			logger.lifecycle("Project specified no JDK version in its gradle.properties. Using default JDK version: $DEFAULT_JDK_VERSION")
			DEFAULT_JDK_VERSION
		}

		val kotlinExtension = project.getKotlinExtension()
		val androidExtension = project.getAndroidExtension()
		val androidComponentExtension = project.getAndroidComponentsExtension()

		// Apply presets to Kotlin projects
		kotlinExtension.applyPreset(project, jdkVersion)

		// Apply presets common to both applications and libraries
		androidExtension.applyAndroidBasePreset(project, jdkVersion)

		// Apply presets specific to applications
		(androidExtension as? ApplicationExtension)?.applyAppPreset(project)

		// Exclude library version files on release builds
		androidComponentExtension.onVariants { variant ->
			if (variant.buildType == "release") {
				variant.packaging.resources.excludes.add("META-INF/*.version")
			}
		}
	}

	private fun KotlinProjectExtension.applyPreset(project: Project, jdkVersion: Int) {
		// Set the JVM toolchain to the configured JDK version
		jvmToolchain(jdkVersion)
	}

	private fun CommonExtension.applyAndroidBasePreset(project: Project, jdkVersion: Int) {
		// Enable BuildConfig
		buildFeatures.buildConfig = true

		// Set source and target compatibility to the configured JDK version
		compileOptions.apply {
			getJavaVersion(jdkVersion).let {
				sourceCompatibility = it
				targetCompatibility = it
			}
		}

		// Lint settings
		lint.abortOnError = false

		// Set JvmTarget to the configured JDK version
		project.tasks.withType(KotlinCompile::class.java) { task ->
			// Annotation targets (Kotlin 2.2+) https://github.com/Kotlin/KEEP/blob/change-defaulting-rule/proposals/annotation-target-in-properties.md
			task.compilerOptions.freeCompilerArgs.add("-Xannotation-default-target=param-property")

			// Let Kotlin target JVM 17
			task.compilerOptions.jvmTarget.set(getJvmTarget(jdkVersion))
		}
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

	private fun Project.getKotlinExtension(): KotlinProjectExtension {
		return extensions.findByType(KotlinProjectExtension::class.java)
			?: throw GradleException("Kotlin Gradle Plugin has not been applied before")
	}

	private fun Project.getAndroidExtension(): CommonExtension {
		return extensions.findByType(ApplicationExtension::class.java)
			?: extensions.findByType(LibraryExtension::class.java)
			?: throw GradleException("Android Gradle Plugin (application or library) has not been applied before")
	}

	private fun Project.getAndroidComponentsExtension(): AndroidComponentsExtension<*, *, *> {
		return extensions.findByType(ApplicationAndroidComponentsExtension::class.java)
			?: extensions.findByType(LibraryAndroidComponentsExtension::class.java)
			?: throw GradleException("Android Gradle Plugin (application or library) has not been applied before")
	}

	private fun getJavaVersion(version: Int) = when (version) {
		in 1..10 -> throw IllegalArgumentException("Java version $version is too old")
		11 -> JavaVersion.VERSION_11
		17 -> JavaVersion.VERSION_17
		21 -> JavaVersion.VERSION_21
		25 -> JavaVersion.VERSION_25
		else -> throw IllegalArgumentException("Unsupported Java version: $version (only LTS versions are supported)")
	}

	private fun getJvmTarget(version: Int) = when (version) {
		in 1..10 -> throw IllegalArgumentException("Java version $version is too old")
		11 -> JvmTarget.JVM_11
		17 -> JvmTarget.JVM_17
		21 -> JvmTarget.JVM_21
		// JvmTarget.JVM_25 does not exist yet...
		else -> throw IllegalArgumentException("Unsupported Java version: $version (only LTS versions are supported)")
	}

}
