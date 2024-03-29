@file:Suppress("Detekt:MagicNumber")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.androidApp.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id("io.github.islamkhsh.xdimen")
}

android {
    namespace = "io.github.islamkhsh.xdimen.sample.kts"
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
    designWidth.set(600)
    designDpi.set(mdpi())
    targetDevicesWidth.set(devicesInPortrait)
    dimensRange {
        minDimen.set(-10)
        maxDimen.set(500)
        step.set(0.1)
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
