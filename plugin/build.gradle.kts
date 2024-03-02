import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    `java-gradle-plugin`
    `maven-publish`
    alias(libs.plugins.pluginPublish)
    alias(libs.plugins.kotlinJVM)
    alias(libs.plugins.ktlint)
}

group = "io.github.islamkhsh"
version = "0.0.8"

gradlePlugin {
    plugins {
        @Suppress("Detekt:UnusedPrivateMember")
        val plugin by creating {
            id = "io.github.islamkhsh.xdimen"
            displayName = "Xdimen"
            description = "Easily support android multiple screen sizes"
            implementationClass = "XdimenPlugin"
            tags = listOf("android", "dimensions", "tablet", "multiple-screens")
        }
    }
}

gradlePlugin {
    website = "https://islamkhsh.github.io/Xdimen/"
    vcsUrl = "https://github.com/IslamKhSh/xdimen"
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
