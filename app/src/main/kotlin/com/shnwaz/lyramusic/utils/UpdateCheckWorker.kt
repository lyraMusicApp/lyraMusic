/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.shnwaz.lyramusic.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.map
import com.shnwaz.lyramusic.BuildConfig
import com.shnwaz.lyramusic.constants.EnableUpdateNotificationKey
import kotlinx.coroutines.flow.first

class UpdateCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val dataStore = applicationContext.dataStore

            val isEnabled = dataStore.data.map { it[EnableUpdateNotificationKey] ?: true }.first()
            if (!isEnabled) return Result.success()

            Updater.getLatestVersionName(forceRefresh = true).onSuccess { latestVersion ->
                if (!Updater.isSameVersion(latestVersion, BuildConfig.VERSION_NAME)) {
                    UpdateNotificationManager.notifyIfNewVersion(applicationContext, latestVersion)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
