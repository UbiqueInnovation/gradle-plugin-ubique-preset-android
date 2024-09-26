import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.kotlinAndroid)
	id("ch.ubique.gradle.preset")
}

android {
	namespace = "ch.ubique.preset.example"
	compileSdk = 34

	defaultConfig {
		applicationId = "ch.ubique.preset.example"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "0.0.1"

		testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
	}

	productFlavors {
		create("prod") {
			if (applicationIdSuffix.isNullOrEmpty().not()) {
				throw AssertionError("Flavor prod has an applicationIdSuffix '$applicationIdSuffix' but shouldn't")
			}
		}
		create("dev") {
			if (applicationIdSuffix != ".dev") {
				throw AssertionError("Flavor dev has a wrong applicationIdSuffix: $applicationIdSuffix")
			}
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
}

tasks.withType<KotlinCompile> {
	compilerOptions.jvmTarget = JvmTarget.JVM_17
	kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
}

dependencies {
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.lifecycle.viewmodelKtx)
}
