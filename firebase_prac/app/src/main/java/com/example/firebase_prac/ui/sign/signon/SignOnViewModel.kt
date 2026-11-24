package com.example.firebase_prac.ui.sign.signon

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_prac.model.service.AccountService
import com.example.firebase_prac.model.service.LogService
import com.example.firebase_prac.model.service.impl.AccountServiceImpl
import com.example.firebase_prac.model.service.impl.LogServiceImpl
import com.example.firebase_prac.ui.*
import kotlinx.coroutines.launch

class SignOnViewModel(
    private val accountService: AccountService = AccountServiceImpl(),
    private val logService: LogService = LogServiceImpl()
) : ViewModel() {
    var uiState = mutableStateOf(SignOnUiState())
        private set

    val email: String
        get() = uiState.value.email

    val password: String
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignOnClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            try {
                accountService.authenticate(email, password)
                openAndPopUp(TASKS_SCREEN, SIGN_ON_SCREEN)
            } catch (e: Exception) {
                logService.logNonFatal(e)
            }
        }
    }

    fun onSignUpClick(openScreen: (String) -> Unit) {
        openScreen(SIGN_UP_SCREEN)
    }
}

data class SignOnUiState(
    val email: String = "",
    val password: String = ""
)
