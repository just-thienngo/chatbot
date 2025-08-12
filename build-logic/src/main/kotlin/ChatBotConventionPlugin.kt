// File: build-logic/src/main/kotlin/ApplicationConventionPlugin.kt

import com.android.build.api.dsl.ApplicationExtension
import com.tkjen.chatbot.ChatBotConfig
import com.tkjen.chatbot.configureBuildType
import com.tkjen.chatbot.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ChatBotConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.android")
            // Có thể áp dụng Hilt ở đây nếu mọi module app đều dùng
            // pluginManager.apply("com.google.dagger.hilt.android")

            extensions.configure<ApplicationExtension> {

                configureKotlinAndroid(this)
                configureBuildType(this)

                // 2. Thêm các cấu hình chỉ dành riêng cho module application
                namespace = "com.example.chatbot"

                defaultConfig {
                    targetSdk = ChatBotConfig.compileSDK // Dùng lại biến cho nhất quán
                    versionCode = 1
                    versionName = "1.0"
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                // Bật các tính năng build nếu cần
                buildFeatures {
                    buildConfig = true
                }


            }
        }
    }
}