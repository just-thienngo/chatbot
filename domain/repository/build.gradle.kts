@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("chatbot-android-library")
    id("chatbot-hilt")
}

android {
    namespace = "com.example.repository"


    defaultConfig {


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }


}

dependencies {

    implementation(libs.bundles.ui)
    implementation(project(mapOf("path" to ":core-common")))
    implementation(project(mapOf("path" to ":domain:common-entity")))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore-ktx")
    // Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:[4,5)")

}