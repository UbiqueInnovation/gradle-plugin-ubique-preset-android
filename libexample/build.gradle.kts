plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("ch.ubique.gradle.preset")
}

android {
    namespace = "ch.ubique.preset.example"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.viewmodelKtx)
}
