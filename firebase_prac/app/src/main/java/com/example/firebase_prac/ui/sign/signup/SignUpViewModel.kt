package com.example.firebase_prac.ui.sign.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_prac.model.service.AccountService
import com.example.firebase_prac.model.service.LogService
import com.example.firebase_prac.model.service.impl.AccountServiceImpl
import com.example.firebase_prac.model.service.impl.LogServiceImpl
import com.example.firebase_prac.ui.SIGN_UP_SCREEN
import com.example.firebase_prac.ui.TASKS_SCREEN
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val accountService: AccountService = AccountServiceImpl(),
    private val logService: LogService = LogServiceImpl()
) : ViewModel() {
    var uiState = mutableStateOf(SignUpUiState())
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

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            try {
                if (accountService.hasUser) {
                    accountService.linkAccount(email, password)
                } else {
                    accountService.createAccount(email, password)
                }
                openAndPopUp(TASKS_SCREEN, SIGN_UP_SCREEN)
            } catch (e: Exception) {
                logService.logNonFatal(e)
            }
        }
    }
}

data class SignUpUiState(
    val email: String = "",
    val password: String = ""
)
