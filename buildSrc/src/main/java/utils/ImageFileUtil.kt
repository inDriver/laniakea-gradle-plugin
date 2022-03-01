package utils

import java.io.File
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

object ImageFileUtil {

    private const val DEFAULT_IMAGE_FILE_NAME = "graph"
    private const val MAX_FILTER_AMOUNT_IN_FILE_NAME = 4

    private val dateFormatter by lazy {
        SimpleDateFormat("ddMM-HHmmss", Locale.getDefault())
    }

    fun creteImageFile(filters: List<String>): File {
        val imageDirectory = File(
            System.getProperty("user.dir") +
                    "/${PluginUtils.LANIAKEA_DIRECTORY}/${PluginUtils.GRAPH_IMAGES_DIRECTORY}"
        )
        if (!imageDirectory.exists()) {
            val isImageDirectoryCreated = imageDirectory.mkdirs()
            if (!isImageDirectoryCreated) {
                throw IllegalStateException("Can't create directory for laniakea images!")
            }
        }

        val fileName = if (filters.isEmpty()) {
            DEFAULT_IMAGE_FILE_NAME
        } else {
            filters.take(MAX_FILTER_AMOUNT_IN_FILE_NAME)
                .joinToString("-") {
                    it.replace(":", "")
                }
        }
        val timestamp = dateFormatter.format(Date().time)
        val filePath = "${imageDirectory.path}/$fileName-$timestamp.png"
        return File(filePath)
    }
}
