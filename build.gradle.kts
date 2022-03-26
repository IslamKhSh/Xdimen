import com.android.build.gradle.BaseExtension
import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJVM) apply false
    alias(libs.plugins.androidApp) apply false

    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply<KtlintPlugin>()

    ktlint {
        verbose.set(true)
        android.set(extensions.findByType(BaseExtension::class.java) != null)
        outputToConsole.set(true)
        enableExperimentalRules.set(true)
        reporters { reporter(ReporterType.HTML) }
        filter { exclude { it.file.path.contains("generated/") } }
    }
}

subprojects {
    afterEvaluate {
        configureAndroidProjectsSourceSets()
    }
}

fun Project.configureAndroidProjectsSourceSets() {
    (project.extensions.findByType(BaseExtension::class.java))?.run {
        sourceSets {
            map { it.java.srcDir("src/${it.name}/kotlin") }
        }
    }
}

tasks.withType<Detekt> { jvmTarget = "1.8" }

tasks.register("ktlintFormatAll") {
    group = project.tasks.ktlintFormat.get().group ?: "formatting"
    description = "Reformat all projects with ktlint"

    dependsOn(tasks.ktlintFormat)
    subprojects.forEach { dependsOn(it.tasks.ktlintFormat) }
    gradle.includedBuilds.forEach { dependsOn(it.task(":${tasks.ktlintFormat.name}")) }
}

val detektAll by tasks.registering(Detekt::class) {
    description = "Run Detekt for all projects"

    parallel = true
    setSource(files(projectDir))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**")
    config.setFrom(files("$rootDir/detekt-config.yml"))
    buildUponDefaultConfig = false
}