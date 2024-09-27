package ch.ubique.gradle.preset.utils

import org.gradle.api.Project
import java.util.*

/**
 * Read a property from the project properties or from a local.properties file
 *
 * @return The value of the property or null if it is not found
 */
fun <T> Project.readProperty(propertyName: String) = readPropertyWithDefault<T?>(propertyName, null)

/**
 * Read a property from the project properties or from a local.properties file
 *
 * @param propertyName The key of the property to be read
 * @param default The default value to be returned if the property is not found
 * @return The value of the property or the default value if it is not found
 */
@Suppress("UNCHECKED_CAST")
fun <T> Project.readPropertyWithDefault(propertyName: String, default: T): T {
	if (this.hasProperty(propertyName)) {
		return this.properties[propertyName] as T
	} else {
		val localPropertiesFileName = "local.properties"

		val properties = Properties()
		if (this.file(localPropertiesFileName).exists()) {
			properties.load(this.file(localPropertiesFileName).inputStream())
		} else if (this.rootProject.file(localPropertiesFileName).exists()) {
			properties.load(this.rootProject.file(localPropertiesFileName).inputStream())
		}

		return if (properties.containsKey(propertyName)) {
			properties.getProperty(propertyName) as T
		} else {
			default
		}
	}
}