package com.arturo254.opentune.utils

import android.content.Context
import android.os.Build
import com.arturo254.opentune.BuildConfig
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TelegramCrashReporter {
    private const val BOT_TOKEN = "8652158842:AAG3KHeNp6mMyuANYji23H5-hujmWI3hsNo"
    private const val CHAT_ID = "-1003003858404"
    private const val TOPIC_ID = 137

    fun sendCrashReport(context: Context?, stackTrace: String) {
        val thread = Thread {
            try {
                val osVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
                val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}".trim()
                val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                val escapedStack = stackTrace
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")

                val trimmedStack = if (escapedStack.length > 3200) {
                    escapedStack.take(3200) + "\n... [truncated]"
                } else {
                    escapedStack
                }

                val message = """
                    <b>💨 Live App Crash Report</b>
                    💱 <b>Device:</b> $deviceModel
                    ⚡️ <b>OS:</b> $osVersion
                    📦 <b>Version:</b> $appVersion
                    🕒 <b>Time:</b> $timestamp
                    
                    ≠️ <b>Stack Trace:</b>
                    <pre>$trimmedStack</pre>
                """.trimIndent()

                val url = URL("https://api.telegram.org/bot$BOT_TOKEN/sendMessage")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                conn.doOutput = true
                conn.connectTimeout = 4000
                conn.readTimeout = 4000

                val payload = JSONObject().apply {
                    put("chat_id", CHAT_ID)
                    put("message_thread_id", TOPIC_ID)
                    put("text", message)
                    put("parse_mode", "HTML")
                }

                conn.outputStream.use { os ->
                    os.write(payload.toString().toByteArray(Charsets.UTF_8))
                }
                conn.responseCode
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        thread.start()
        try {
            thread.join(2500)
        } catch (_: InterruptedException) {}
    }
}
