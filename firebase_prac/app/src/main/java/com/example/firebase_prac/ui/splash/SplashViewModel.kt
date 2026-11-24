package com.example.firebase_prac.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_prac.model.service.AccountService
import com.example.firebase_prac.model.service.ConfigurationService
import com.example.firebase_prac.model.service.LogService
import com.example.firebase_prac.model.service.impl.AccountServiceImpl
import com.example.firebase_prac.model.service.impl.ConfigurationServiceImpl
import com.example.firebase_prac.model.service.impl.LogServiceImpl
import com.example.firebase_prac.ui.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SplashViewModel(
    private val configurationService: ConfigurationService = ConfigurationServiceImpl(),
    private val accountService: AccountService = AccountServiceImpl(),
    private val logService: LogService = LogServiceImpl()
) : ViewModel() {

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            try {
                configurationService.fetchConfiguration()
            } catch (e: Exception) {
                logService.logNonFatal(e)
            }

            if (accountService.hasUser) {
                openAndPopUp(TASKS_SCREEN, SPLASH_SCREEN)
            } else {
                createAnonymousAccount(openAndPopUp)
            }
        }
    }

    private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            logService.logNonFatal(throwable)
            openAndPopUp(SIGN_ON_SCREEN, SPLASH_SCREEN)
        }) {
            accountService.authenticateAnonymous()
            openAndPopUp(TASKS_SCREEN, SPLASH_SCREEN)
        }
    }
}
