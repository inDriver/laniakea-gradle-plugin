package com.indriver.laniakea.utils

import java.io.File
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

object FileUtil {

    private const val DEFAULT_FILE_NAME = "graph"
    private const val MAX_FILTER_AMOUNT_IN_FILE_NAME = 4

    private val dateFormatter by lazy {
        SimpleDateFormat("dd.MM.yy-HH.mm.ss", Locale.getDefault())
    }

    fun creteImageFile(filters: List<String>, fileType: FileType): File {
        val fileDirectory = File(
            System.getProperty("user.dir") +
                    "/${PluginUtils.LANIAKEA_DIRECTORY}/${fileType.fileDirectory}"
        )
        if (!fileDirectory.exists()) {
            val isFileDirectoryCreated = fileDirectory.mkdirs()
            if (!isFileDirectoryCreated) {
                throw IllegalStateException("Can't create directory for Laniakea file with type ${fileType.type}!")
            }
        }

        val fileName = if (filters.isEmpty()) {
            DEFAULT_FILE_NAME
        } else {
            filters.take(MAX_FILTER_AMOUNT_IN_FILE_NAME)
                .joinToString("-") {
                    it.replace(":", "")
                }
        }
        val timestamp = dateFormatter.format(Date().time)
        val filePath = "${fileDirectory.path}/$timestamp-$fileName${fileType.type}"
        return File(filePath)
    }

    enum class FileType(val type: String, val fileDirectory: String) {
        PNG(".png", PluginUtils.GRAPH_IMAGES_DIRECTORY),
        DOT(".dot", PluginUtils.GRAPH_DOT_DIRECTORY)
    }
}
