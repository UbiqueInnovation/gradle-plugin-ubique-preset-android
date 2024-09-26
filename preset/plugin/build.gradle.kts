import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    alias(libs.plugins.pluginPublish)
    alias(libs.plugins.jfrog.artifactory)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())

    implementation(libs.agp)
    implementation(libs.kotlin.gradle)

    implementation(libs.coroutines)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofitConverterScalars)
    implementation(libs.moshi)
    implementation(libs.moshiKotlin)
    implementation(libs.moshiAdapters)

    testImplementation(libs.junit)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget = JvmTarget.JVM_17
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
}

gradlePlugin {
    plugins {
        create(property("ID").toString()) {
            id = property("ID").toString()
            implementationClass = property("IMPLEMENTATION_CLASS").toString()
            version = project.version
            description = property("DESCRIPTION").toString()
            displayName = property("DISPLAY_NAME").toString()
            tags = listOf("android", "ubique")
        }
    }
    website.set(property("WEBSITE").toString())
    vcsUrl.set(property("VCS_URL").toString())
}

tasks.create("setupPluginUploadFromEnvironment") {
    doLast {
        val key = System.getenv("GRADLE_PUBLISH_KEY")
        val secret = System.getenv("GRADLE_PUBLISH_SECRET")
        if (key == null || secret == null) {
            throw GradleException("GRADLE_PUBLISH_KEY and/or GRADLE_PUBLISH_SECRET are not defined environment variables")
        }
        System.setProperty("gradle.publish.key", key)
        System.setProperty("gradle.publish.secret", secret)
    }
}
