package com.yariv.ofek.taxicounter.presentation.calculation.compose

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yariv.ofek.taxicounter.R
import com.yariv.ofek.taxicounter.other.util.UiText
import com.yariv.ofek.taxicounter.presentation.calculation.CalculationEvent
import com.yariv.ofek.taxicounter.presentation.calculation.CalculationFragmentViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CalculationFragment(
    viewModel: CalculationFragmentViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    val secondTextFieldFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
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
                onValueChange = { viewModel.onEvent(CalculationEvent.OnOriginQueryChanged(it)) },
                label = UiText.StringResource(R.string.origin).asString(),
                suggestions = state.originSuggestions,
                onSuggestionSelected = {
                    viewModel.onEvent(CalculationEvent.OnOriginQueryChanged(it))
                    if (state.destination.isEmpty()) {
                        secondTextFieldFocusRequester.requestFocus()
                    } else {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                },
                hasLocationFeature = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomOutlinedTextField(
                state = state.destination,
                onValueChange = { viewModel.onEvent(CalculationEvent.OnDestinationQueryChanged(it)) },
                label = UiText.StringResource(R.string.destination).asString(),
                suggestions = state.destinationSuggestions,
                onSuggestionSelected = {
                    viewModel.onEvent(CalculationEvent.OnDestinationQueryChanged(it))
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                focusRequester = secondTextFieldFocusRequester,
            )
            Spacer(modifier = Modifier.height(16.dp))
            TimePickerRow(
                context = context,
                dateTime = state.selectedDateTime,
                onDateTimeSelected = { viewModel.onEvent(
                    CalculationEvent.OnSelectedDateTimeChanged(
                        it
                    )
                ) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            ) {
            ExtendedFloatingActionButton(
                onClick = { viewModel.onEvent(CalculationEvent.OnCalculateClicked) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = UiText.StringResource(R.string.calculate).asString())
            }
        }
    }
}