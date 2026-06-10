package ch.ubique.gradle.preset.config

import org.gradle.api.Project
import javax.inject.Inject

@Suppress("UnnecessaryAbstractClass")
abstract class PresetPluginConfig
@Inject
constructor(project: Project) {
	private val objects = project.objects
}
