package com.example.firebase_prac.model.service.impl

import com.example.firebase_prac.model.service.ConfigurationService
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

class ConfigurationServiceImpl : ConfigurationService {

    override suspend fun fetchConfiguration(): Boolean {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        return remoteConfig.fetchAndActivate().await()
    }

    override val isShowTaskEditButtonConfig: Boolean
        get() = Firebase.remoteConfig.getBoolean(SHOW_TASK_EDIT_BUTTON_KEY)

    companion object {
        private const val SHOW_TASK_EDIT_BUTTON_KEY = "show_task_edit_button"
    }
}
