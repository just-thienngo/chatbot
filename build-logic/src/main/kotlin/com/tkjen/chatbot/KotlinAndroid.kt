package com.tkjen.chatbot

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *>)

{
    commonExtension.apply{
        compileSdk = ChatBotConfig.compileSDK

        defaultConfig {
            minSdk = ChatBotConfig.minSDK

        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        tasks.withType<KotlinCompile>().configureEach {
           kotlinOptions {
                    jvmTarget = "1.8"
            }
        }

    }

}