import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("chatbot-android-library")
    id("chatbot-hilt")
    alias(libs.plugins.secrets)
}

// Hàm đọc giá trị từ file hoặc ENV
fun readConfigValue(key: String): String {
    val propsFile = file("${project.projectDir}/release.properties")
    return if (propsFile.exists()) {
        Properties().apply { load(propsFile.inputStream()) }
            .getProperty(key, "")
    } else {
        System.getenv(key.uppercase()) ?: ""
    }
}

// Đọc các giá trị cần thiết
val apiKey: String by lazy { readConfigValue("apiKey") }
val baseUrl: String by lazy { readConfigValue("baseUrl") }

secrets {
    defaultPropertiesFileName = "${project.projectDir}/release.properties"
}

android {
    namespace = "com.example.remote"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"${apiKey.trim()}\"")
            buildConfigField("String", "BASE_URL", "\"${baseUrl.trim()}\"")
        }
        release {
            buildConfigField("String", "API_KEY", "\"${apiKey.trim()}\"")
            buildConfigField("String", "BASE_URL", "\"${baseUrl.trim()}\"")
        }
    }

}

dependencies {
    implementation(libs.bundles.ui)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(project(":core-common"))
    implementation(libs.bundles.coroutines)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}
