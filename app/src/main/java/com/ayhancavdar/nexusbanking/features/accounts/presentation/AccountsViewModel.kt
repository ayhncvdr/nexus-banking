/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accounts.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayhancavdar.nexusbanking.core.common.NBNetworkException
import com.ayhancavdar.nexusbanking.core.di.IoDispatcher
import com.ayhancavdar.nexusbanking.features.accounts.domain.repository.AccountsRepository
import com.ayhancavdar.nexusbanking.features.accounts.presentation.state.AccountsState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val ACCOUNTS_STATE_KEY = "accounts_state"
private const val BALANCE_FORMAT = "%,.0f"
private const val DEFAULT_CURRENCY = "TL"
private const val DEFAULT_AVAILABLE_BALANCE = 0.0

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val accountsRepository: AccountsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    //region State
    val uiState: StateFlow<AccountsState> = savedStateHandle.getStateFlow(
        ACCOUNTS_STATE_KEY,
        AccountsState()
    )
    //endregion

    //region Initialization
    init {
        getAccounts()
    }

    private fun getAccounts() {
        viewModelScope.launch(ioDispatcher) {
            updateUiState { it.copy(isLoading = true) }
            val result = accountsRepository.getAccounts()
            result.fold(
                onSuccess = { response ->
                    val accounts = response.accounts.orEmpty()
                    val totalBalance = accounts.sumOf { account ->
                        account.availableBalance?.toDoubleOrNull() ?: DEFAULT_AVAILABLE_BALANCE
                    }
                    val formattedBalance = String.format(Locale.getDefault(), BALANCE_FORMAT, totalBalance)
                    val defaultCurrency = accounts.firstOrNull()?.currency ?: DEFAULT_CURRENCY

                    updateUiState { currentState ->
                        currentState.copy(
                            accounts = accounts,
                            filteredAccounts = accounts,
                            totalAvailableBalance = formattedBalance,
                            defaultCurrency = defaultCurrency,
                            isLoading = false
                        )
                    }
                },
                onFailure = { exception ->
                    val errorMessage = if (exception is NBNetworkException && !exception.errorMessage.isNullOrEmpty()) {
                        exception.errorMessage
                    } else {
                        exception.message
                    }

                    updateUiState {
                        it.copy(
                            isLoading = false,
                            accountsApiError = errorMessage
                        )
                    }
                }
            )
        }
    }
    //endregion

    //region UI State Management
    private fun updateUiState(update: (AccountsState) -> AccountsState) {
        val newState = update(uiState.value)
        savedStateHandle[ACCOUNTS_STATE_KEY] = newState
    }
    //endregion

    //region Event Handlers
    fun onSearchTextChanged(searchText: String) {
        updateUiState { currentState ->
            val filteredAccounts = if (searchText.isEmpty()) {
                currentState.accounts
            } else {
                currentState.accounts.filter { account ->
                    account.name?.contains(searchText, ignoreCase = true) == true ||
                        account.branchName?.contains(searchText, ignoreCase = true) == true
                }
            }
            currentState.copy(
                searchText = searchText,
                filteredAccounts = filteredAccounts
            )
        }
    }

    fun onClearSearch() {
        updateUiState { currentState ->
            currentState.copy(
                searchText = "",
                filteredAccounts = currentState.accounts
            )
        }
    }

    fun onLogoutClick() {
        updateUiState { currentState ->
            currentState.copy(
                showLogoutDialog = true
            )
        }
    }

    fun onLogoutConfirm() {
        updateUiState { currentState ->
            currentState.copy(
                showLogoutDialog = false
            )
        }
    }

    fun onLogoutCancel() {
        updateUiState { currentState ->
            currentState.copy(
                showLogoutDialog = false
            )
        }
    }
    //endregion
}
