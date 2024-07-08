buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}
