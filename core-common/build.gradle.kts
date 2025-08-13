@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("chatbot-android-library")
    id("chatbot-hilt")
}

android {
    namespace = "com.example.code.common"


    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

}

dependencies {

    implementation(libs.bundles.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    // Loading Button
    implementation("com.github.leandroborgesferreira:loading-button-android:2.3.0")
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
}