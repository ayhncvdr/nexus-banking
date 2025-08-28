// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.dagger.hilt.android") version "2.56.2" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20" apply false
    id("androidx.navigation.safeargs") version "2.9.3" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.8" apply false
}