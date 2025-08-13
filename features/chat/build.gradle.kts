@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("chatbot-android-library")
    id("chatbot-hilt")
}

android {
    namespace = "com.example.chat"


    defaultConfig {


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

}

dependencies {

    implementation(project(mapOf("path" to ":domain:usecase")))
    implementation(project(mapOf("path" to ":domain:common-entity")))
    implementation(project(mapOf("path" to ":data:remote")))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    // UI
    implementation(libs.bundles.ui)

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    // Coroutines
    implementation(libs.bundles.coroutines)
    implementation(project(mapOf("path" to ":core-common")))
    implementation(project(mapOf("path" to ":navigation")))
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore-ktx")
    // Google Play Services
    implementation("com.google.android.gms:play-services-auth:20.0.0")
    implementation("com.google.android.gms:play-services-measurement:21.2.0")

    // Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:[4,5)")
    implementation("com.github.leandroborgesferreira:loading-button-android:2.3.0")

    implementation(libs.navigation.ui.ktx)
    implementation(libs.navigation.fragment.ktx)
}