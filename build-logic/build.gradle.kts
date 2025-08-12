plugins{
    `kotlin-dsl`
}
dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)

}
gradlePlugin{
    plugins {
        register("ChatBotConventionPlugin") {
            id = "chatbot-android-application"
            implementationClass = "ChatBotConventionPlugin"
        }
        register("ChatBotLibraryConventionPlugin") {
            id = "chatbot-android-library"
            implementationClass = "ChatBotLibraryConventionPlugin"
        }
        register("HiltConventionPlugin") {
            id = "chatbot-hilt"
            implementationClass = "HiltConventionPlugin"
        }
    }
}