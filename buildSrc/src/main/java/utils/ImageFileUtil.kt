package utils

import java.io.File
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

object ImageFileUtil {

    private const val GRAPH_IMAGES_DIRECTORY = "laniakeaImages"
    private const val DEFAULT_IMAGE_FILE_NAME = "graph"

    private val dateFormatter by lazy {
        SimpleDateFormat("ddMMHHmmss", Locale.getDefault())
    }

    fun creteImageFile(filters: List<String>): File {
        val imageDirectory = File("./$GRAPH_IMAGES_DIRECTORY")
        if (!imageDirectory.exists()) {
            val isImageDirectoryCreated = imageDirectory.mkdir()
            if (!isImageDirectoryCreated) {
                throw IllegalStateException("Can't create directory for laniakea images!")
            }
        }

        val fileName = if (filters.isEmpty()) {
            DEFAULT_IMAGE_FILE_NAME
        } else {
            filters.take(2).joinToString("-") { it.replace(":", "") }
        }
        val timestamp = dateFormatter.format(Date().time)
        val filePath = "${imageDirectory.path}/$fileName-$timestamp.png"
        return File(filePath)
    }
}
