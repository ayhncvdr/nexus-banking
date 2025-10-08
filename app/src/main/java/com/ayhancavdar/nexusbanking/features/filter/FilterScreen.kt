/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.filter

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.ui.components.NBPrimaryButton
import com.ayhancavdar.nexusbanking.core.ui.theme.NBColors
import com.ayhancavdar.nexusbanking.core.ui.theme.NexusBankingTheme
import com.ayhancavdar.nexusbanking.features.filter.state.CurrencyFilter
import com.ayhancavdar.nexusbanking.features.filter.state.FilterParameters
import com.ayhancavdar.nexusbanking.features.filter.state.FilterState
import com.ayhancavdar.nexusbanking.features.filter.state.label
import java.util.Calendar
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

private val BORDER_WIDTH = 1.dp

@Composable
fun FilterScreen(
    viewModel: FilterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onApplyFilter: (FilterParameters) -> Unit = {},
    initialFilterParameters: FilterParameters? = null,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(initialFilterParameters) {
        initialFilterParameters?.let { params ->
            viewModel.initializeWithParameters(params)
        }
    }

    FilterScreenContent(
        uiState = uiState,
        onSelectCurrency = viewModel::onCurrencySelected,
        onSelectStartDate = viewModel::onStartDateSelected,
        onSelectEndDate = viewModel::onEndDateSelected,
        onResetFilter = viewModel::onResetFilter,
        onNavigateBack = onNavigateBack,
        onApplyFilter = {
            onApplyFilter(
                FilterParameters(
                    selectedCurrency = uiState.selectedCurrency,
                    startDateMillis = uiState.startDateMillis,
                    endDateMillis = uiState.endDateMillis
                )
            )
        },
        formatDate = viewModel::formatDate
    )
}

@Composable
private fun FilterScreenContent(
    uiState: FilterState,
    onSelectCurrency: (CurrencyFilter) -> Unit,
    onSelectStartDate: (Long) -> Unit,
    onSelectEndDate: (Long) -> Unit,
    onResetFilter: () -> Unit,
    onNavigateBack: () -> Unit,
    onApplyFilter: () -> Unit,
    formatDate: (Long) -> String,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            FilterAppBar(
                onNavigateBack = onNavigateBack,
                onResetFilter = onResetFilter
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            NBColors.primaryGreen.copy(alpha = 0.05f),
                            NBColors.offWhite,
                            NBColors.primaryGreen.copy(alpha = 0.02f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensionResource(id = R.dimen.x24)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.x8)))

                // Header Section with Logo
                FilterHeaderSection()

                Spacer(Modifier.height(dimensionResource(id = R.dimen.x40)))

                // Filter Card
                FilterCard(
                    uiState = uiState,
                    onSelectCurrency = onSelectCurrency,
                    onSelectStartDate = onSelectStartDate,
                    onSelectEndDate = onSelectEndDate,
                    formatDate = formatDate,
                    onApplyFilter = onApplyFilter
                )

                Spacer(Modifier.height(dimensionResource(id = R.dimen.x24)))
            }
        }
    }
}

@Composable
private fun FilterHeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "NexusBanking Logo",
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.x128))
                .aspectRatio(1f)
                .shadow(
                    elevation = dimensionResource(id = R.dimen.x4),
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .background(Color.White),
            contentScale = ContentScale.FillBounds
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x24)))

        Text(
            text = stringResource(id = R.string.filter_navigationBar_title),
            style = MaterialTheme.typography.headlineMedium,
            color = NBColors.nearBlack,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x8)))

        Text(
            text = "Apply filters to customize your account view",
            style = MaterialTheme.typography.bodyLarge,
            color = NBColors.primaryGreenLight,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FilterCard(
    uiState: FilterState,
    onSelectCurrency: (CurrencyFilter) -> Unit,
    onSelectStartDate: (Long) -> Unit,
    onSelectEndDate: (Long) -> Unit,
    formatDate: (Long) -> String,
    onApplyFilter: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = dimensionResource(id = R.dimen.x8),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.x16)),
                clip = false
            ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.x16)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.x24))
        ) {
            Text(
                text = "Filter Options",
                style = MaterialTheme.typography.titleLarge,
                color = NBColors.nearBlack,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x24))
            )

            // Currency Selection Section
            Text(
                text = "Currency",
                style = MaterialTheme.typography.bodyLarge,
                color = NBColors.nearBlack,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x12))
            )

            CurrencySelectionRow(
                selectedCurrency = uiState.selectedCurrency,
                onSelectCurrency = onSelectCurrency,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x24))
            )

            // Date Range Section
            Text(
                text = "Date Range",
                style = MaterialTheme.typography.bodyLarge,
                color = NBColors.nearBlack,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x12))
            )

            DateRangeCard(
                startDateMillis = uiState.startDateMillis,
                endDateMillis = uiState.endDateMillis,
                onSelectStartDate = onSelectStartDate,
                onSelectEndDate = onSelectEndDate,
                formatDate = formatDate,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x24))
            )

            // Apply Filter Button
            NBPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.x56)),
                onClick = onApplyFilter,
                content = {
                    Text(
                        stringResource(id = R.string.filter_filterButton_title).uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.x12)),
                enabled = true,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterAppBar(
    onNavigateBack: () -> Unit,
    onResetFilter: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.x24)),
                    tint = NBColors.nearBlack
                )
            }
        },
        actions = {
            TextButton(onClick = onResetFilter) {
                Text(
                    text = "RESET",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NBColors.primaryGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@Composable
private fun CurrencySelectionRow(
    selectedCurrency: CurrencyFilter,
    onSelectCurrency: (CurrencyFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currencies = listOf(
        CurrencyFilter.All,
        CurrencyFilter.Specific("TL"),
        CurrencyFilter.Specific("EUR"),
        CurrencyFilter.Specific("USD")
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        currencies.forEach { currency ->
            CurrencyOption(
                currency = currency,
                isSelected = currency == selectedCurrency,
                onSelectCurrency = { onSelectCurrency(currency) }
            )
        }
    }
}

@SuppressLint("LocalContextResourcesRead")
@Composable
private fun CurrencyOption(
    currency: CurrencyFilter,
    isSelected: Boolean,
    onSelectCurrency: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val resources = LocalContext.current.resources

    Box(
        modifier = modifier
            .size(width = dimensionResource(id = R.dimen.x64), height = dimensionResource(id = R.dimen.x40))
            .background(
                color = if (isSelected) NBColors.primaryGreen else NBColors.lightGrey,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.x8))
            )
            .border(
                width = BORDER_WIDTH,
                color = if (isSelected) NBColors.primaryGreen else NBColors.mediumGrey,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.x8))
            )
            .clickable { onSelectCurrency() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = currency.label(resources).uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) Color.White else NBColors.nearBlack,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun DateRangeCard(
    startDateMillis: Long,
    endDateMillis: Long,
    onSelectStartDate: (Long) -> Unit,
    onSelectEndDate: (Long) -> Unit,
    formatDate: (Long) -> String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NBColors.lightGrey
        ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.x12))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.x16)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DateColumn(
                title = "Start Date",
                dateMillis = startDateMillis,
                isStartDate = true,
                onSelectDate = onSelectStartDate,
                otherDateMillis = endDateMillis,
                formatDate = formatDate,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.x24))
                    .background(NBColors.mediumGrey)
                    .size(width = 1.dp, height = dimensionResource(id = R.dimen.x40))
            )

            DateColumn(
                title = "End Date",
                dateMillis = endDateMillis,
                isStartDate = false,
                onSelectDate = onSelectEndDate,
                otherDateMillis = startDateMillis,
                formatDate = formatDate,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateColumn(
    title: String,
    dateMillis: Long,
    isStartDate: Boolean,
    onSelectDate: (Long) -> Unit,
    otherDateMillis: Long,
    formatDate: (Long) -> String,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val minDateMillis = if (isStartDate) {
        FilterState.getDefaultStartDateMillis()
    } else {
        otherDateMillis
    }

    val maxDateMillis = System.currentTimeMillis()

    val constrainedDateMillis = max(minDateMillis, min(dateMillis, maxDateMillis))

    val minYear = getYearFromMillis(minDateMillis)
    val maxYear = getYearFromMillis(maxDateMillis)
    val constrainedYear = getYearFromMillis(constrainedDateMillis)
    val originalYear = getYearFromMillis(dateMillis)

    val finalMinYear = minOf(minYear, constrainedYear, originalYear)
    val finalMaxYear = maxOf(maxYear, constrainedYear, originalYear)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.x8)))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = NBColors.nearBlack,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.x12)))

        Box(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.x8))
                )
                .border(
                    width = BORDER_WIDTH,
                    color = NBColors.mediumGrey,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.x8))
                )
                .clickable { showDatePicker = true }
                .padding(
                    horizontal = dimensionResource(id = R.dimen.x12),
                    vertical = dimensionResource(id = R.dimen.x8)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = formatDate(dateMillis),
                style = MaterialTheme.typography.bodySmall,
                color = NBColors.nearBlack
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.x8)))
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = constrainedDateMillis,
            yearRange = finalMinYear..finalMaxYear,
            initialDisplayMode = DisplayMode.Picker,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis in minDateMillis..maxDateMillis
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return year in finalMinYear..finalMaxYear
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { onSelectDate(it) }
                        showDatePicker = false
                    }
                ) {
                    Text(
                        text = "OK",
                        color = NBColors.primaryGreen
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(
                        text = "Cancel",
                        color = NBColors.primaryGreen
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = NBColors.primaryGreen,
                    todayDateBorderColor = NBColors.primaryGreen,
                    todayContentColor = NBColors.primaryGreen
                )
            )
        }
    }
}

private fun getYearFromMillis(millis: Long): Int {
    val calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = millis
    return calendar.get(Calendar.YEAR)
}

@Preview
@Composable
private fun FilterScreenPreview() {
    NexusBankingTheme {
        FilterScreenContent(
            uiState = FilterState(),
            onSelectCurrency = {},
            onSelectStartDate = {},
            onSelectEndDate = {},
            onResetFilter = {},
            onNavigateBack = {},
            onApplyFilter = {},
            formatDate = { android.text.format.DateFormat.format("dd/MM/yyyy", it).toString() }
        )
    }
}
