/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.common.NBNetworkException
import com.ayhancavdar.nexusbanking.core.common.NBSharedPrefs
import com.ayhancavdar.nexusbanking.core.di.IoDispatcher
import com.ayhancavdar.nexusbanking.core.util.NBRegexps
import com.ayhancavdar.nexusbanking.features.login.domain.model.Credentials
import com.ayhancavdar.nexusbanking.features.login.domain.repository.LoginRepository
import com.ayhancavdar.nexusbanking.features.login.presentation.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val USERNAME_PREF_KEY = "username"
private const val MIN_USERNAME_LENGTH = 6
private const val MAX_USERNAME_LENGTH = 10
private const val MIN_PASSWORD_LENGTH = 6
private const val LOGIN_STATE_KEY = "login_state"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loginRepository: LoginRepository,
    private val sharedPreferences: NBSharedPrefs,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    //region State
    val uiState: StateFlow<LoginState> = savedStateHandle.getStateFlow(
        LOGIN_STATE_KEY,
        LoginState(),
    )
    //endregion

    //region Initialization
    init {
        loadSavedUsername()
    }

    private fun loadSavedUsername() {
        val savedUsername = sharedPreferences.getString(USERNAME_PREF_KEY, "")
        val usernameErrorRes = if (savedUsername.isNotBlank()) validateUsername(savedUsername) else null
        updateUiState { currentState ->
            currentState.copy(
                usernameInput = savedUsername,
                usernameError = usernameErrorRes,
                isLoginButtonEnabled = savedUsername.isNotBlank() &&
                    usernameErrorRes == null &&
                    currentState.passwordError == null &&
                    currentState.isLoading.not(),
            )
        }
    }
    //endregion

    //region UI State Management
    private fun updateUiState(update: (LoginState) -> LoginState) {
        val newState = update(uiState.value)
        savedStateHandle[LOGIN_STATE_KEY] = newState
    }
    //endregion

    //region Event Handlers
    fun onUsernameChanged(input: String) {
        val errorRes = validateUsername(input)
        updateUiState { currentState ->
            currentState.copy(
                usernameInput = input,
                usernameError = errorRes,
                isLoginButtonEnabled = errorRes == null &&
                    currentState.passwordError == null &&
                    currentState.passwordInput.isNotBlank() &&
                    currentState.isLoading.not(),
                loginApiError = null
            )
        }
    }

    fun onPasswordChanged(input: String) {
        val errorRes = validatePassword(input)
        updateUiState { currentState ->
            currentState.copy(
                passwordInput = input,
                passwordError = errorRes,
                isLoginButtonEnabled = currentState.usernameError == null &&
                    currentState.usernameInput.isNotBlank() &&
                    errorRes == null &&
                    currentState.isLoading.not(),
                loginApiError = null
            )
        }
    }

    fun onRememberMeChanged(isChecked: Boolean) {
        updateUiState { currentState ->
            currentState.copy(rememberMe = isChecked)
        }
        if (isChecked.not()) {
            sharedPreferences.remove(USERNAME_PREF_KEY)
        }
    }

    fun onDismissLoginError() {
        updateUiState { it.copy(loginApiError = null) }
    }
    //endregion

    //region Login Action
    fun attemptLogin() {
        val currentState = uiState.value
        val username = currentState.usernameInput
        val password = currentState.passwordInput
        val usernameError = validateUsername(currentState.usernameInput)
        val passwordError = validatePassword(currentState.passwordInput)

        if (currentState.isLoginButtonEnabled.not() || currentState.isLoading) {
            return
        }

        if (usernameError != null || passwordError != null) {
            updateUiState {
                it.copy(
                    usernameError = usernameError,
                    passwordError = passwordError,
                    isLoginButtonEnabled = false,
                )
            }
            return
        }

        viewModelScope.launch(ioDispatcher) {
            updateUiState { it.copy(isLoading = true, isLoginButtonEnabled = false) }

            val credentials = Credentials(username = username, password = password)
            val result = loginRepository.login(credentials)

            result.fold(onSuccess = { user ->
                if (currentState.rememberMe) {
                    sharedPreferences.setString(USERNAME_PREF_KEY, currentState.usernameInput)
                } else {
                    sharedPreferences.remove(USERNAME_PREF_KEY)
                }
                updateUiState {
                    it.copy(
                        isLoading = false,
                        loginApiError = null,
                        customerName = user.customerName,
                        starredSmsNumber = user.smsOtpNumbers.firstOrNull()?.starredNumber,
                        isLoginButtonEnabled = true,
                    )
                }
            }, onFailure = { exception ->
                val errorMessage = if (exception is NBNetworkException && !exception.errorMessage.isNullOrEmpty()) {
                    exception.errorMessage
                } else {
                    exception.message
                }

                updateUiState {
                    it.copy(
                        isLoading = false,
                        loginApiError = errorMessage,
                        isLoginButtonEnabled = (it.usernameError == null) && (it.passwordError == null)
                    )
                }
            })
        }
    }
    //endregion

    //region Validation Functions
    @StringRes
    private fun validateUsername(username: String): Int? {
        return when {
            username.isBlank() -> R.string.login_validation_error_username_required
            username.matches(NBRegexps.SIX_TO_TEN_CHARACTERS.toRegex())
                .not() -> if (username.length < MIN_USERNAME_LENGTH || username.length > MAX_USERNAME_LENGTH) {
                R.string.login_validation_error_username_length
            } else {
                R.string.login_validation_error_username_alphanumeric
            }

            else -> null
        }
    }

    @StringRes
    private fun validatePassword(password: String): Int? {
        return when {
            password.isBlank() -> R.string.login_validation_error_password_required
            password.matches(NBRegexps.EXACTLY_SIX_DIGITS.toRegex())
                .not() -> if (password.length != MIN_PASSWORD_LENGTH) {
                R.string.login_validation_error_password_length
            } else {
                R.string.login_validation_error_password_numeric
            }

            else -> null
        }
    }
    //endregion

    //region Navigation
    fun onNavigationToOtp() {
        updateUiState { it.copy(customerName = null, starredSmsNumber = null) }
    }
    //endregion
}
