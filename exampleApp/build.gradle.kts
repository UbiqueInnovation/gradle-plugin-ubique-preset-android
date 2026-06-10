plugins {
	alias(libs.plugins.android.application)
	id("ch.ubique.gradle.preset")
}

android {
	namespace = "ch.ubique.preset.example"
	compileSdk = 36

	defaultConfig {
		applicationId = "ch.ubique.preset.example"
		minSdk = 26
		targetSdk = 36
		versionCode = 1
		versionName = project.version.toString()
		testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
	}

	productFlavors {
		create("prod") {
			check(applicationIdSuffix.isNullOrEmpty()) {
				"Flavor prod has an applicationIdSuffix '$applicationIdSuffix' but shouldn't"
			}
		}
		create("dev") {
			check(applicationIdSuffix == ".dev") {
				"Flavor dev has a wrong applicationIdSuffix: $applicationIdSuffix"
			}
		}
	}
}

dependencies {
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.lifecycle.viewmodelKtx)
}
