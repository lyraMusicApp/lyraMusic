/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.arturo254.opentune.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.map
import com.arturo254.opentune.BuildConfig
import com.arturo254.opentune.constants.EnableUpdateNotificationKey
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

            Updater.getLatestVersionName().onSuccess { latestVersion ->
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
