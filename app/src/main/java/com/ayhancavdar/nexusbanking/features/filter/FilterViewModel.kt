/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.filter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ayhancavdar.nexusbanking.features.filter.state.CurrencyFilter
import com.ayhancavdar.nexusbanking.features.filter.state.FilterParameters
import com.ayhancavdar.nexusbanking.features.filter.state.FilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

private const val FILTER_STATE_KEY = "filter_state"

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //region State
    val uiState: StateFlow<FilterState> = savedStateHandle.getStateFlow(
        FILTER_STATE_KEY,
        FilterState()
    )
    //endregion

    //region UI State Management
    private fun updateUiState(update: (FilterState) -> FilterState) {
        val newState = update(uiState.value)
        savedStateHandle[FILTER_STATE_KEY] = newState
    }
    //endregion

    fun initializeWithParameters(parameters: FilterParameters) {
        updateUiState {
            it.copy(
                selectedCurrency = parameters.selectedCurrency,
                startDateMillis = parameters.startDateMillis,
                endDateMillis = parameters.endDateMillis
            )
        }
    }

    //region Event Handlers
    fun onCurrencySelected(currency: CurrencyFilter) {
        updateUiState { currentState ->
            currentState.copy(selectedCurrency = currency)
        }
    }

    fun onStartDateSelected(dateMillis: Long) {
        updateUiState { currentState ->
            val newEndDateMillis = if (currentState.endDateMillis < dateMillis) {
                dateMillis
            } else {
                currentState.endDateMillis
            }
            currentState.copy(
                startDateMillis = dateMillis,
                endDateMillis = newEndDateMillis
            )
        }
    }

    fun onEndDateSelected(dateMillis: Long) {
        updateUiState { currentState ->
            currentState.copy(endDateMillis = dateMillis)
        }
    }

    fun onResetFilter() {
        updateUiState {
            FilterState()
        }
    }

    fun formatDate(millis: Long): String =
        android.text.format.DateFormat.format("dd/MM/yyyy", millis).toString()

    //endregion
}
