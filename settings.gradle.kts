include("sample-kts", "sample-groovy")

pluginManagement {
    @Suppress("UnstableApiUsage")
    includeBuild("plugin")

    repositories {
        gradlePluginPortal()
        google()
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "android.android.application" ->
                    useModule("android.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("/plugin/gradle/libs.versions.toml"))
        }
    }
}
