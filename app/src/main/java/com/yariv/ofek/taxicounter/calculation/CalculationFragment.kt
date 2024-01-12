package com.yariv.ofek.taxicounter.calculation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yariv.ofek.taxicounter.R
import com.yariv.ofek.taxicounter.calculation.compose.CustomOutlinedTextField
import com.yariv.ofek.taxicounter.calculation.compose.TimePickerRow
import com.yariv.ofek.taxicounter.other.util.UiText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CalculationFragment(
    viewModel: CalculationFragmentViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val context = LocalContext.current

    val secondTextFieldFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = UiText.StringResource(
                    R.string.calculations_left,
                    state.calculationsLeftForToday
                ).asString(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomOutlinedTextField(
                state = state.origin,
                onValueChange = { viewModel.onOriginQueryChanged(it) },
                label = UiText.StringResource(R.string.origin).asString(),
                suggestions = state.originSuggestions,
                onSuggestionSelected = {
                    viewModel.onOriginQueryChanged(it)
                    if (state.destination.isEmpty()) {
                        secondTextFieldFocusRequester.requestFocus()
                    } else {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomOutlinedTextField(
                state = state.destination,
                onValueChange = { viewModel.onDestinationQueryChanged(it) },
                label = UiText.StringResource(R.string.destination).asString(),
                suggestions = state.destinationSuggestions,
                onSuggestionSelected = {
                    viewModel.onDestinationQueryChanged(it)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                focusRequester = secondTextFieldFocusRequester,
            )
            Spacer(modifier = Modifier.height(16.dp))
            TimePickerRow(
                context = context,
                dateTime = state.selectedDateTime,
                onDateTimeSelected = { viewModel.onDateTimeSelected(it) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            ) {
            ExtendedFloatingActionButton(
                onClick = { viewModel.onCalculationButtonClick() },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = UiText.StringResource(R.string.calculate).asString())
            }
        }
    }
}