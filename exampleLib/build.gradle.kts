plugins {
    alias(libs.plugins.android.library)
    id("ch.ubique.gradle.preset")
}

android {
    namespace = "ch.ubique.preset.example"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
		testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.lifecycle.viewmodelKtx)
}
