plugins {
    id("chatbot-android-application")
    id("chatbot-hilt")
    alias(libs.plugins.google.gms.google.services)
}

dependencies {

    // UI
    implementation(libs.bundles.ui)

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    implementation(project(mapOf("path" to ":core-common")))
    implementation(project(mapOf("path" to ":features:auth")))
    implementation(project(mapOf("path" to ":features:chat")))
    implementation(project(mapOf("path" to ":domain:repository")))
    implementation(project(mapOf("path" to ":domain:usecase")))
    implementation(project(mapOf("path" to ":data:repository-impl")))

    // Coroutines
    implementation(libs.bundles.coroutines)

    // Navigation
    implementation(libs.bundles.navigation)



    // Retrofit + Moshi + OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)

    implementation(libs.okhttp.logging.interceptor)

    // Glide
    implementation(libs.glide)

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

    // Loading Button
    implementation("com.github.leandroborgesferreira:loading-button-android:2.3.0")

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)

    // Ads
    implementation(project(mapOf("path" to ":ads")))


}