package com.cattv.app.data

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ChannelLogger {

    private const val LOG_FILE = "failed_channels.log"

    fun logFailedChannel(context: Context, channelName: String, url: String, error: String) {
        try {
            val file = File(context.getExternalFilesDir(null), LOG_FILE)
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Date())
            val entry = "[$timestamp] FAILED | $channelName | $url | $error\n"
            file.appendText(entry)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logSuccessChannel(context: Context, channelName: String) {
        try {
            val file = File(context.getExternalFilesDir(null), LOG_FILE)
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Date())
            val entry = "[$timestamp] OK    | $channelName\n"
            file.appendText(entry)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLogPath(context: Context): String {
        return File(context.getExternalFilesDir(null), LOG_FILE).absolutePath
    }

    fun getFailedChannels(context: Context): List<String> {
        return try {
            val file = File(context.getExternalFilesDir(null), LOG_FILE)
            if (!file.exists()) return emptyList()
            file.readLines().filter { it.contains("FAILED") }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
