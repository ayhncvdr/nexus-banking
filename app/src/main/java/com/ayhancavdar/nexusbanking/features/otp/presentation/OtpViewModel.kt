/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.otp.presentation

import android.os.SystemClock
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.common.NBNetworkException
import com.ayhancavdar.nexusbanking.core.di.IoDispatcher
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute
import com.ayhancavdar.nexusbanking.core.util.NBRegexps
import com.ayhancavdar.nexusbanking.features.otp.domain.repository.OtpRepository
import com.ayhancavdar.nexusbanking.features.otp.presentation.state.OtpState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val OTP_TIMER_DURATION_SECONDS = 120L
private const val MIN_PASSWORD_LENGTH = 6
private const val DELAY_TIME = 1000L
private const val TIME_FORMAT = "%02d:%02d"
private const val TIMER_END_TIME_KEY = "otp_timer_end_time"
private const val OTP_STATE_KEY = "otp_state"

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val otpRepository: OtpRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val otpArgs: NexusBankingRoute.Otp = savedStateHandle.toRoute()
    private var countdownJob: Job? = null

    //region State
    val uiState: StateFlow<OtpState> = savedStateHandle.getStateFlow(
        OTP_STATE_KEY,
        OtpState(starredSmsNumber = otpArgs.starredSmsNumber)
    )
    //endregion

    //region Initialization
    init {
        restoreOrStartTimer()
    }

    private fun restoreOrStartTimer() {
        val endTime = savedStateHandle.get<Long>(TIMER_END_TIME_KEY)
        val now = SystemClock.elapsedRealtime()

        if (endTime != null && endTime > now) {
            val remainingSeconds = (endTime - now) / DELAY_TIME
            startOtpTimer(remainingSeconds)
        } else {
            startOtpTimer(OTP_TIMER_DURATION_SECONDS)
        }
    }

    private fun startOtpTimer(durationSeconds: Long) {
        countdownJob?.cancel()
        val endTime = SystemClock.elapsedRealtime() + durationSeconds * DELAY_TIME
        savedStateHandle[TIMER_END_TIME_KEY] = endTime

        updateUiState {
            it.copy(
                isTimerRunning = true,
                canResendOtp = false
            )
        }

        countdownJob = viewModelScope.launch(ioDispatcher) {
            for (secondsDown in durationSeconds downTo 0) {
                updateUiState {
                    it.copy(
                        countdownSeconds = secondsDown.toInt(),
                        formattedCountdown = formatTime(secondsDown),
                        sliderProgress = secondsDown.toFloat() / OTP_TIMER_DURATION_SECONDS
                    )
                }
                delay(DELAY_TIME)
            }

            updateUiState {
                it.copy(
                    isTimerRunning = false, canResendOtp = true, sliderProgress = 0.0f
                )
            }
        }
    }

    private fun formatTime(seconds: Long): String {
        val minutes = seconds.seconds.inWholeMinutes
        val remainingSeconds = seconds.seconds - minutes.minutes
        return String.format(Locale.getDefault(), TIME_FORMAT, minutes, remainingSeconds.inWholeSeconds)
    }
    //endregion

    //region UI State Management
    private fun updateUiState(update: (OtpState) -> OtpState) {
        val newState = update(uiState.value)
        savedStateHandle[OTP_STATE_KEY] = newState
    }
    //endregion

    //region Event Handlers
    fun onOtpChanged(input: String) {
        val errorRes = validateOtp(input)
        updateUiState { currentState ->
            currentState.copy(
                otpInput = input,
                otpError = errorRes,
                isContinueButtonEnabled = errorRes == null &&
                    currentState.isLoading.not() &&
                    currentState.countdownSeconds > 0,
                otpApiError = null
            )
        }
    }

    fun onDismissOtpError() {
        updateUiState { it.copy(otpApiError = null) }
    }
    //endregion

    //region Validation Functions
    @StringRes
    private fun validateOtp(otpInput: String): Int? {
        return when {
            otpInput.isBlank() -> R.string.login_validation_error_password_required
            otpInput.matches(NBRegexps.EXACTLY_SIX_DIGITS.toRegex())
                .not() -> if (otpInput.length != MIN_PASSWORD_LENGTH) {
                R.string.login_validation_error_password_length
            } else {
                R.string.login_validation_error_password_numeric
            }

            else -> null
        }
    }
    //endregion

    //region Navigation
    fun onNavigationToAccounts() {
        updateUiState { it.copy(navigateToAccounts = false) }
    }
    //endregion

    //region Otp Action
    fun attemptOtpLogin() {
        val currentState = uiState.value
        val otpInput = currentState.otpInput
        val otpError = validateOtp(currentState.otpInput)

        if (currentState.isContinueButtonEnabled.not() || currentState.isLoading) {
            return
        }

        if (otpError != null) {
            updateUiState { it.copy(otpError = otpError, isContinueButtonEnabled = false) }
            return
        }

        viewModelScope.launch(ioDispatcher) {
            updateUiState { it.copy(isLoading = true, isContinueButtonEnabled = false) }
            val result = otpRepository.otp(otpInput)

            result.fold(onSuccess = {
                updateUiState { currentState ->
                    currentState.copy(
                        isLoading = false,
                        otpApiError = null,
                        navigateToAccounts = true,
                        isContinueButtonEnabled = true,
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
                        otpApiError = errorMessage,
                        isContinueButtonEnabled = (it.otpError == null)
                    )
                }
            })
        }
    }
    //endregion
}
