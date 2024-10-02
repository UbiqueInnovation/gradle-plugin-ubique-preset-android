package ch.ubique.gradle.preset.utils

import org.gradle.api.Project
import java.util.*

/**
 * Read a property from the project properties or from a local.properties file
 *
 * @return The value of the property or null if it is not found
 */
fun Project.readProperty(propertyName: String): String? {
	if (this.hasProperty(propertyName)) {
		return this.properties[propertyName] as? String?
	} else {
		val localPropertiesFileName = "local.properties"

		val properties = Properties()
		if (this.file(localPropertiesFileName).exists()) {
			properties.load(this.file(localPropertiesFileName).inputStream())
		} else if (this.rootProject.file(localPropertiesFileName).exists()) {
			properties.load(this.rootProject.file(localPropertiesFileName).inputStream())
		}

		return if (properties.containsKey(propertyName)) {
			properties.getProperty(propertyName)
		} else {
			null
		}
	}
}

/**
 * Read a property from the project properties or from a local.properties file
 *
 * @param propertyName The key of the property to be read
 * @param default The default value to be returned if the property is not found
 * @return The value of the property or the default value if it is not found
 */
fun Project.readPropertyWithDefault(propertyName: String, default: String) = readProperty(propertyName) ?: default