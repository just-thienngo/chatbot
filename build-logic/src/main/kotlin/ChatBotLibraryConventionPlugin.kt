// File: build-logic/src/main/kotlin/ApplicationLibraryConventionPlugin.kt - ĐÃ SỬA

import com.android.build.api.dsl.LibraryExtension
import com.tkjen.chatbot.configureBuildType
import com.tkjen.chatbot.configureKotlinAndroid

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ChatBotLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")

            // SỬA: Sử dụng LibraryExtension cho các module thư viện
            extensions.configure<LibraryExtension> {
                // Tái sử dụng hàm cấu hình chung của bạn
                configureKotlinAndroid(this)

                // Bạn có thể giữ lại các cấu hình chung cho buildType ở đây
                configureBuildType(this)

                defaultConfig {
                    // Consumer Proguard files là cấu hình phổ biến cho thư viện
                    consumerProguardFile("consumer-rules.pro")

                }
                buildFeatures {
                    buildConfig = true // Bật tính năng này
                    viewBinding = true
                    dataBinding = true // Bật Data Binding nếu cần
                }
            }
        }
    }
}