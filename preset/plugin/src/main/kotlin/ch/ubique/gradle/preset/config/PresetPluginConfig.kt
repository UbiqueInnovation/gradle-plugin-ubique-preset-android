package ch.ubique.gradle.preset.config

import org.gradle.api.Project
import org.gradle.api.provider.Property
import javax.inject.Inject

@Suppress("UnnecessaryAbstractClass")
abstract class PresetPluginConfig
@Inject
constructor(project: Project) {
	private val objects = project.objects

	val jdkVersion: Property<Int> = objects.property(Int::class.java)

}
