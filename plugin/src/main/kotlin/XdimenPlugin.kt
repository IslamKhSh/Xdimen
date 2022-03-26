import com.android.build.gradle.BaseExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import pluginExtensions.DimenRange
import pluginExtensions.XdimenExtension
import pluginExtensions.initDimenRangeDefaultValues
import pluginExtensions.initFontsRangeDefaultValues
import tasks.deleteXdimen.DeleteXdimenResTask
import tasks.generateXdimen.GenerateXdimenResTask

class XdimenPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // validate that this plugin is applied in android project
        project.extensions.findByType(BaseExtension::class.java)
            ?: throw GradleException("Project ${project.name} is not an Android project")

        val extension = project.extensions.create("xdimen", XdimenExtension::class.java)

        // register task for deleting xdimen files
        project.tasks.register("deleteXdimen", DeleteXdimenResTask::class.java)

        // register task for generating xdimen files
        val generateXdimenTask = project.tasks.register("generateXdimen", GenerateXdimenResTask::class.java) {
            if (extension.deleteOldXdimen.getOrElse(true)) {
                it.dependsOn("deleteXdimen")
            }
        }

        generateXdimenTask.configure {
            it.designWidth.set(extension.designWidth) // no default value, designWidth is required
            it.designDpi.value(extension.designDpi.orNull).convention(extension.mdpi())

            // As the `SetProperty` has an initial value empty set,
            // this workaround used to add the default list to the initial empty set
            if (extension.targetDevicesWidth.orNull?.isEmpty() == true) {
                extension.targetDevicesWidth.addAll(extension.devicesInPortrait)
            }
            it.targetDevicesWidth.value(extension.targetDevicesWidth).convention(extension.devicesInPortrait)

            it.dimensRange.value(extension.dimesRange)
                .convention(project.objects.property(DimenRange::class.java))
                .get().initDimenRangeDefaultValues()

            it.fontDimesRange.value(extension.fontDimesRange)
                .convention(project.objects.property(DimenRange::class.java))
                .get().initFontsRangeDefaultValues()
        }
    }
}
