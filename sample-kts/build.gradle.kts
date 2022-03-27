@file:Suppress("Detekt:MagicNumber")

@Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")
plugins {
    id(libs.plugins.androidApp.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id("io.github.islamkhsh.xdimen")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "io.github.islamkhsh.xdimen.sample.kts"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

xdimen {
    deleteOldXdimen.set(true)
    designWidth.set(411)
    designDpi.set(mdpi())
    targetDevicesWidth.set(phonePortrait + 411)
    dimensRange {
        minDimen.set(-10)
        maxDimen.set(500)
        step.set(0.5)
    }
    fontsRange {
        minDimen.set(10)
        maxDimen.set(60)
        step.set(1.0)
    }
}

dependencies {
    implementation(libs.kotlinStd)
    implementation(libs.appcompat)
}
