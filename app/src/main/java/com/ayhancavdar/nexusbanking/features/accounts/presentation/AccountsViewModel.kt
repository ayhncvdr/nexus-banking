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
import androidx.navigation.toRoute
import com.ayhancavdar.nexusbanking.core.common.NBNetworkException
import com.ayhancavdar.nexusbanking.core.di.IoDispatcher
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute
import com.ayhancavdar.nexusbanking.features.accounts.data.model.Account
import com.ayhancavdar.nexusbanking.features.accounts.domain.repository.AccountsRepository
import com.ayhancavdar.nexusbanking.features.accounts.presentation.state.AccountsState
import com.ayhancavdar.nexusbanking.features.filter.state.CurrencyFilter
import com.ayhancavdar.nexusbanking.features.filter.state.FilterParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.IllegalFormatException
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val ACCOUNTS_STATE_KEY = "accounts_state"
private const val BALANCE_FORMAT = "%,.0f"
private const val DEFAULT_CURRENCY = "TL"
private const val DEFAULT_AVAILABLE_BALANCE = 0.0
private const val DATE_FORMAT = "dd.MM.yyyy"
private const val TIMEZONE_UTC = "UTC"

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val accountsRepository: AccountsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val accountsArgs: NexusBankingRoute.Accounts = savedStateHandle.toRoute()

    //region State
    val uiState: StateFlow<AccountsState> = savedStateHandle.getStateFlow(
        ACCOUNTS_STATE_KEY,
        AccountsState(customerName = accountsArgs.customerName)
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
                    val accounts = response.accounts
                    val totalBalance = accounts.sumOf { account ->
                        account.availableBalance?.toDoubleOrNull() ?: DEFAULT_AVAILABLE_BALANCE
                    }
                    val formattedBalance = try {
                        String.format(Locale.getDefault(), BALANCE_FORMAT, totalBalance)
                    } catch (e: IllegalFormatException) {
                        "0"
                    }
                    val defaultCurrency = accounts.firstOrNull()?.currency ?: DEFAULT_CURRENCY

                    updateUiState { currentState ->
                        val filteredAccounts =
                            applyFilters(accounts, currentState.searchText, currentState.appliedFilters)
                        currentState.copy(
                            accounts = accounts,
                            filteredAccounts = filteredAccounts,
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
        synchronized(savedStateHandle) {
            savedStateHandle[ACCOUNTS_STATE_KEY] = update(uiState.value)
        }
    }
    //endregion

    //region Event Handlers
    fun onSearchTextChanged(searchText: String) {
        updateUiState { currentState ->
            val filteredAccounts = applyFilters(
                accounts = currentState.accounts,
                searchText = searchText,
                filterParameters = currentState.appliedFilters,
            )
            currentState.copy(
                searchText = searchText,
                filteredAccounts = filteredAccounts
            )
        }
    }

    fun onClearSearch() {
        updateUiState { currentState ->
            val filteredAccounts = applyFilters(currentState.accounts, "", currentState.appliedFilters)
            currentState.copy(
                searchText = "",
                filteredAccounts = filteredAccounts
            )
        }
    }

    fun onFiltersApplied(filterParameters: FilterParameters) {
        updateUiState { currentState ->
            val filteredAccounts = applyFilters(
                accounts = currentState.accounts,
                searchText = currentState.searchText,
                filterParameters = filterParameters,
            )

            currentState.copy(
                appliedFilters = filterParameters,
                filteredAccounts = filteredAccounts
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

    //region Filter Logic
    private fun applyFilters(
        accounts: List<Account>,
        searchText: String,
        filterParameters: FilterParameters?
    ): List<Account> {
        var filteredAccounts = accounts

        if (searchText.isNotEmpty()) {
            filteredAccounts = filteredAccounts.filter { account ->
                account.name?.contains(searchText, ignoreCase = true) == true ||
                    account.branchName?.contains(searchText, ignoreCase = true) == true
            }
        }

        filterParameters?.let { filters ->
            filteredAccounts = filteredAccounts.filter { account ->
                matchesCurrencyFilter(account, filters) && matchesDateFilter(account, filters)
            }
        }

        return filteredAccounts
    }

    private fun matchesCurrencyFilter(
        account: Account,
        filterParameters: FilterParameters,
    ): Boolean {
        return when (filterParameters.selectedCurrency) {
            is CurrencyFilter.All -> true
            is CurrencyFilter.Specific ->
                account.currency?.equals(filterParameters.selectedCurrency.code, ignoreCase = true) == true
        }
    }

    private fun matchesDateFilter(
        account: Account,
        filterParameters: FilterParameters
    ): Boolean {
        val accountOpeningDate = parseAccountOpeningDate(account.openingDate) ?: return true

        val startDate = Date(filterParameters.startDateMillis)
        val endDate = Date(filterParameters.endDateMillis)

        val normalizedStartDate = normalizeToStartOfDay(startDate)
        val normalizedEndDate = normalizeToEndOfDay(endDate)
        val normalizedAccountDate = normalizeToStartOfDay(accountOpeningDate)

        return !normalizedAccountDate.before(normalizedStartDate) && !normalizedAccountDate.after(normalizedEndDate)
    }

    private fun parseAccountOpeningDate(openingDateString: String?): Date? {
        if (openingDateString.isNullOrEmpty()) return null

        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

        try {
            formatter.timeZone = java.util.TimeZone.getTimeZone(TIMEZONE_UTC)
            val date = formatter.parse(openingDateString)
            return date
        } catch (e: ParseException) {
            /* No op */
        }

        return null
    }

    private fun normalizeToStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone(TIMEZONE_UTC)).apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.time
    }

    private fun normalizeToEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone(TIMEZONE_UTC)).apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.time
    }
    //endregion
}
