import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("chatbot-android-library")
    id("chatbot-hilt")
   // alias(libs.plugins.secrets)
}

// Hàm đọc giá trị config
fun readConfigValue(key: String): String {
    val isCI = System.getenv("GITHUB_ACTIONS") == "true"
    return if (isCI) {
        // Khi chạy trên GitHub Actions → luôn lấy từ ENV
        System.getenv(key.uppercase()) ?: ""
    } else {
        // Khi chạy local → ưu tiên file release.properties, fallback ENV
        val propsFile = file("${project.projectDir}/release.properties")
        if (propsFile.exists()) {
            Properties().apply { load(propsFile.inputStream()) }
                .getProperty(key, "")
        } else {
            System.getenv(key.uppercase()) ?: ""
        }
    }
}

// Đọc từ file hoặc ENV
val apiKey: String by lazy { readConfigValue("apiKey") }
val baseUrl: String by lazy { readConfigValue("baseUrl") }

//secrets {
//    defaultPropertiesFileName = "${project.projectDir}/release.properties"
//}

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
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
