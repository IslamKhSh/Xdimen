package tasks.deleteXdimen

import utils.Constants.DP
import utils.Constants.RESOURCES_PATH
import utils.Constants.VALUES_ALTER_RES_DIR_PREFIX
import utils.Constants.VALUES_RES_DIR
import utils.Constants.XDIMEN_RES_FILE_NAME
import java.io.File

internal open class XdimenResRemoverHelper {

    var isAnyFileDeleted = false
        private set

    /**
     * find res dirs that named `values` or `values-sw...dp` in rootResourceDir,
     * then delete files named with [XDIMEN_RES_FILE_NAME] under these dirs.
     */
    fun getAndRemoveXdimenRes(projectDir: File) {
        val valuesAlterResDirNameRegex = "$VALUES_ALTER_RES_DIR_PREFIX[\\d]+$DP".toRegex() // values-sw...dp

        val rootResourceDir = File(projectDir, RESOURCES_PATH)

        rootResourceDir.listFiles()?.filter {
            it.name == VALUES_RES_DIR || it.name.matches(valuesAlterResDirNameRegex)
        }?.forEach {
            removeXdimenResFile(it, projectDir)
        }
    }

    /**
     * delete file named xdimen.xml under this dir, also delete the dir if it has only xdimen.xml file
     *
     * @param valuesResDir the `values` resource directory or one of its alternatives to delete xdimen file from.
     * @param projectDir the directory of project, use it to calculate the relative path of deleted files.
     * @return the deleted xdimen res file or null if no files with this name found.
     */
    private fun removeXdimenResFile(valuesResDir: File, projectDir: File) {
        valuesResDir.listFiles()?.find { it.name == XDIMEN_RES_FILE_NAME }?.let {
            it.delete()
            println("Resource file deleted: ${it.relativeTo(projectDir).invariantSeparatorsPath}")
            isAnyFileDeleted = true

            // delete dir if it becomes empty
            if (valuesResDir.listFiles()?.isEmpty() == true) {
                valuesResDir.delete()
            }
        }
    }
}
