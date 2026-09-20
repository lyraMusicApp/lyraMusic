package com.shnwaz.lyramusic.utils

import android.content.Context
import android.os.Build
import com.shnwaz.lyramusic.BuildConfig
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object TelegramCrashReporter {
    private const val BOT_TOKEN = "8652158842:AAG3KHeNp6mMyuANYji23H5-hujmWI3hsNo"
    private const val CHAT_ID = "-1003003858404"
    private const val TOPIC_ID = 137

    fun sendCrashReport(context: Context?, stackTrace: String) {
        val thread = Thread {
            try {
                val osVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
                val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}".trim()
                val appVersion = "v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})"

                val lines = stackTrace.lines()
                val firstLine = lines.firstOrNull().orEmpty()
                val exceptionType = firstLine.substringBefore(":").ifBlank { "Exception" }
                val exceptionMsg = firstLine.substringAfter(":", "").trim().ifBlank { "Uncaught exception" }

                val previewLines = lines.take(6).joinToString("\n")
                val previewWithNote = "$previewLines\n... [view full on Telegra.ph]"

                val escapedException = exceptionType.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                val escapedMsg = exceptionMsg.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                val escapedPreview = previewWithNote.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")

                val message = """
<b>Lyra Music | Bot</b>  <code>admin</code>
💥 <b>Lyra Music App Crash Detected!</b> ⚠️

🏷️ <b>Version:</b> $appVersion
📱 <b>Device:</b> $deviceModel • $osVersion
🛑 <b>Exception:</b> <code>$escapedException</code>
💬 <b>Message:</b> <code>$escapedMsg</code>

📋 <b>Stack Trace Preview:</b>
<pre>$escapedPreview</pre>

📄 <b>Full Logs:</b> <a href="https://telegra.ph">View Full Crash Log on Telegra.ph</a>

📊 <i>Live sync to Topic 137 • Direct In-App Crash Reporter</i>
                """.trimIndent()

                val button = JSONObject().apply {
                    put("text", "📄 Open Full Crash Log on Telegra.ph ↗")
                    put("url", "https://telegra.ph")
                }
                val row = JSONArray().apply { put(button) }
                val inlineKeyboard = JSONArray().apply { put(row) }
                val replyMarkup = JSONObject().apply { put("inline_keyboard", inlineKeyboard) }

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
                    put("reply_markup", replyMarkup)
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