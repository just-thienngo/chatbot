

import com.android.build.api.dsl.ApplicationExtension
import com.tkjen.chatbot.ChatBotConfig
import com.tkjen.chatbot.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File

class ChatBotConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.android")
            // Có thể áp dụng Hilt ở đây nếu mọi module app đều dùng
            // pluginManager.apply("com.google.dagger.hilt.android")

            extensions.configure<ApplicationExtension> {
                // Áp dụng cấu hình Kotlin/Android chung
                configureKotlinAndroid(this)

                // Cấu hình chỉ dành riêng cho module application
                namespace = "com.example.chatbot"

                defaultConfig {
                    targetSdk = ChatBotConfig.compileSDK
                    versionCode = 1
                    versionName = "1.0"
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildFeatures {
                    buildConfig = true
                    viewBinding = true
                    dataBinding = true
                }

                // --- ĐỊNH NGHĨA CÁC CẤU HÌNH KÝ ---
                signingConfigs {
                    create("releaseConfig") {
                        // Lấy đường dẫn keystore từ biến môi trường
                        storeFile = System.getenv("KEYSTORE_PATH")?.let { File(it) } ?: run {
                            logger.warn("KEYSTORE_PATH environment variable is not set or file not found. Release build might be unsigned.")
                            File("dummy.jks")
                        }
                        storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
                        keyAlias = System.getenv("KEY_ALIAS") ?: ""
                        keyPassword = System.getenv("KEY_PASSWORD") ?: ""
                    }
                }

                // --- CẤU HÌNH CÁC BUILD TYPE CỤ THỂ CHO APPLICATION ---
                buildTypes {
                    getByName("debug") {
                        isMinifyEnabled = false // Không minify cho debug
                    }
                    getByName("release") {
                        isMinifyEnabled = false // Minify cho release (thường là true)
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                        signingConfig = signingConfigs.getByName("releaseConfig")
                    }
                }
            }
        }
    }
}