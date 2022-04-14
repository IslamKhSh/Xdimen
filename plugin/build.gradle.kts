import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    `java-gradle-plugin`
    `maven-publish`
    alias(libs.plugins.pluginPublish)
    alias(libs.plugins.kotlinJVM)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.gradleVersions)
    alias(libs.plugins.versionCatalogUpdate)
}

group = "io.github.islamkhsh"
version = "0.0.6"

gradlePlugin {
    plugins {
        @Suppress("Detekt:UnusedPrivateMember")
        val plugin by creating {
            id = "io.github.islamkhsh.xdimen"
            displayName = "Xdimen"
            description = "Easily support android multiple screen sizes"
            implementationClass = "XdimenPlugin"
        }
    }
}

pluginBundle {
    website = "https://islamkhsh.github.io/Xdimen/"
    vcsUrl = "https://github.com/IslamKhSh/xdimen"
    tags = listOf("android", "dimensions", "tablet", "multiple-screens")
}

repositories {
    google()
    mavenCentral()
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        @Suppress("Detekt:UnusedPrivateMember")
        val functionalTest by registering(JvmTestSuite::class) {
            testType.set(TestSuiteType.FUNCTIONAL_TEST)

            dependencies {
                implementation(libs.jupiterApi)
                implementation(libs.jupiterEngine)
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(libs.kotlinStd)
    implementation(libs.androidBuildTools)
    implementation(gradleApi())
    implementation(libs.xmlBuilder)

    testImplementation(libs.jupiterApi)
    testRuntimeOnly(libs.jupiterEngine)
    testImplementation(libs.mockk)
    "functionalTestImplementation"(gradleTestKit())
}

ktlint {
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    enableExperimentalRules.set(true)
    reporters { reporter(ReporterType.HTML) }
    filter { exclude { it.file.path.contains("generated/") } }
}

@Suppress("UnstableApiUsage")
tasks.named("check") {
    dependsOn(testing.suites.named("functionalTest"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

versionCatalogUpdate {
    sortByKey.set(false)
    pin {
        versions.set(setOf("agpVersion"))
        libraries.set(setOf(libs.xmlBuilder))
        plugins.set(setOf(libs.plugins.versionCatalogUpdate))
    }
    keep {
        keepUnusedVersions.set(true)
        keepUnusedLibraries.set(true)
        keepUnusedPlugins.set(true)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}