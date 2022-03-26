package tasks.deleteXdimen

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import utils.Constants
import utils.TextColor
import utils.styleOutput

abstract class DeleteXdimenResTask : DefaultTask() {

    private val xdimenResRemoverHelper by lazy { project.objects.newInstance(XdimenResRemoverHelper::class.java) }

    init {
        group = Constants.TASKS_GROUP
        description = "Delete all xdimen files"
    }

    @TaskAction
    fun deleteCreatedXdimen() {
        xdimenResRemoverHelper.getAndRemoveXdimenRes(project.projectDir)

        if (xdimenResRemoverHelper.isAnyFileDeleted) {
            println("==========================================")
            styleOutput(textColor = TextColor.Green, boldText = true) {
                println("Old Xdimen resources deleted successfully.")
            }
            println("==========================================")
        } else {
            styleOutput(textColor = TextColor.Yellow, boldText = true) {
                println("There's no any Xdimen resources to delete.")
            }
        }
    }
}
