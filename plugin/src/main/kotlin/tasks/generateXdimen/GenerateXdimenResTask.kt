package tasks.generateXdimen

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import pluginExtensions.DPI
import pluginExtensions.DimenRange
import utils.Constants
import utils.TextColor
import utils.styleOutput

abstract class GenerateXdimenResTask : DefaultTask() {

    @get:Input
    abstract val designWidth: Property<Int>

    @get:Input
    abstract val designDpi: Property<DPI>

    @get:Input
    abstract val dimensRange: Property<DimenRange>

    @get:Input
    abstract val fontDimesRange: Property<DimenRange>

    @get:Input
    abstract val targetDevicesWidth: SetProperty<Int>

    private val xdimenGeneratorHelper by lazy {
        XdimenResGeneratorHelper(project.projectDir, dimensRange.get(), fontDimesRange.get())
    }

    init {
        group = Constants.TASKS_GROUP
        description = "Generate Xdimen files"
    }

    @TaskAction
    fun generateXdimen() {
        // validate user inputs
        require(designWidth.get() > 0) { "designWidth can't be negative or 0, but found $designWidth." }
        require(designDpi.get().value > 0) {
            "designDpi value can't be negative or 0, but found ${designDpi.get().value}."
        }
        require(targetDevicesWidth.get().isNotEmpty()) {
            "empty list of target devices' width. ${targetDevicesWidth.get()}"
        }
        validateDimenRange(dimensRange.get())
        validateDimenRange(fontDimesRange.get())

        xdimenGeneratorHelper.generateResources(
            relativeDesignWidth = xdimenGeneratorHelper.calculateRelativeWidth(designWidth.get(), designDpi.get()),
            targetDevicesWidth = targetDevicesWidth.get()
        )

        println("======================================")
        styleOutput(textColor = TextColor.Green, boldText = true) {
            println("Xdimen resources created successfully.")
        }
        println("======================================")
    }

    private fun validateDimenRange(dimenRange: DimenRange) {
        require(dimenRange.maxDimen.get() >= dimenRange.minDimen.get()) {
            "maxDimen (${dimenRange.maxDimen.get()}) must be more or equal minDimen (${dimenRange.minDimen.get()})."
        }
        require(dimenRange.step.get() > 0.0) {
            "step can't be negative or 0, but found ${dimenRange.step.get()}."
        }
    }
}
