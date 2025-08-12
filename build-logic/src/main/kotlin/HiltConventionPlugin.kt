// File: build-logic/src/main/kotlin/HiltConventionPlugin.kt

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Lấy ra version catalog "libs"
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            pluginManager.apply(libs.findPlugin("hilt").get().get().pluginId)
            pluginManager.apply(libs.findPlugin("ksp").get().get().pluginId)

            // 2. Thêm các dependency của Hilt (cách của bạn đã đúng)
            dependencies {
                "implementation"(libs.findLibrary("hilt.android").get())
                "ksp"(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}