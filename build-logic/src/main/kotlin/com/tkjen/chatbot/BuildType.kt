
package com.tkjen.chatbot
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
internal fun Project.configureBuildType(commonExtension: CommonExtension<*, *, *, *, *>) {
    commonExtension.apply {
        buildTypes {
            getByName("debug") {
                isMinifyEnabled = false
            }

            // SỬA LỖI Ở ĐÂY: Dùng getByName cho build type đã tồn tại
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
}