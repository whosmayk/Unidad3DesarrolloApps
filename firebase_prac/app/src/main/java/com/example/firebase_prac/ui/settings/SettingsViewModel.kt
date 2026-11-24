package com.example.firebase_prac.ui.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_prac.model.service.AccountService
import com.example.firebase_prac.model.service.StorageService
import com.example.firebase_prac.model.service.impl.AccountServiceImpl
import com.example.firebase_prac.model.service.impl.StorageServiceImpl
import com.example.firebase_prac.ui.SIGN_ON_SCREEN
import com.example.firebase_prac.ui.SIGN_UP_SCREEN
import com.example.firebase_prac.ui.SPLASH_SCREEN
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val accountService: AccountService = AccountServiceImpl(),
    private val storageService: StorageService = StorageServiceImpl()
) : ViewModel() {
    var uiState = mutableStateOf(SettingsUiState())
        private set

    fun onSignOutClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            accountService.signOut()
            openAndPopUp(SIGN_ON_SCREEN, SPLASH_SCREEN)
        }
    }

    fun onDeleteAccountClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            try {
                storageService.deleteAllForUser(accountService.currentUserId)
                accountService.deleteAccount()
                openAndPopUp(SIGN_ON_SCREEN, SPLASH_SCREEN)
            } catch (e: Exception) {
                // handle exception
            }
        }
    }

    fun onLoginClick(openScreen: (String) -> Unit) {
        openScreen(SIGN_ON_SCREEN)
    }

    fun onSignUpClick(openScreen: (String) -> Unit) {
        openScreen(SIGN_UP_SCREEN)
    }
}

data class SettingsUiState(
    val isAnonymous: Boolean = true
)
