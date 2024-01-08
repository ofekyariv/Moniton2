package com.yariv.ofek.taxicounter.presentation.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.yariv.ofek.taxicounter.R
import com.yariv.ofek.taxicounter.other.util.UiText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CalculationFragment() {
    var startingPoint by remember { mutableStateOf("") }
    var destinationPoint by remember { mutableStateOf("") }
    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var leaveNow by remember { mutableStateOf(true) }
    val calculationsLeftForToday by remember { mutableIntStateOf(3) }

    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = UiText.StringResource(R.string.calculations_left, calculationsLeftForToday)
                    .asString(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = startingPoint,
                onValueChange = { startingPoint = it },
                label = { Text(text = UiText.StringResource(R.string.starting_point).asString()) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { /* TODO: Implement speech-to-text */ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_mic),
                            contentDescription = "Speech"
                        )
                    }
                },
                leadingIcon = {
                    IconButton(onClick = { /* TODO: Implement location */ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_my_location),
                            contentDescription = "Location"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = destinationPoint,
                onValueChange = { destinationPoint = it },
                label = { Text(text = UiText.StringResource(R.string.ending_point).asString()) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { /* TODO: Implement speech-to-text */ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_mic),
                            contentDescription = "Speech"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDateTimePicker(
                            context,
                            selectedDateTime ?: LocalDateTime.now()
                        ) { dateTime, isLeaveNow ->
                            leaveNow = isLeaveNow
                            if (!leaveNow) {
                                selectedDateTime = dateTime
                            }
                        }
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_clock),
                    contentDescription = "Pick Date and Time",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (leaveNow) "Leave Now" else selectedDateTime!!.format(dateFormatter))
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            ExtendedFloatingActionButton(
                onClick = { /* TODO: Implement calculation logic */ },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(200.dp)
            ) {
                Text(text = UiText.StringResource(R.string.calculate).asString())
            }
        }
    }
}

fun showDateTimePicker(
    context: Context,
    currentDateTime: LocalDateTime,
    onDateTimeSelected: (LocalDateTime, Boolean) -> Unit
) {
    DatePickerDialog(context, { _, year, month, dayOfMonth ->
        val newDate = LocalDateTime.of(
            year,
            month + 1,
            dayOfMonth,
            currentDateTime.hour,
            currentDateTime.minute
        )

        TimePickerDialog(context, { _, hour, minute ->
            val selectedDateTime = newDate.withHour(hour).withMinute(minute)
            if (selectedDateTime.isBefore(LocalDateTime.now())) {
                onDateTimeSelected(selectedDateTime, true)
            } else {
                onDateTimeSelected(selectedDateTime, false)
            }
        }, currentDateTime.hour, currentDateTime.minute, true).apply {
            setButton(
                TimePickerDialog.BUTTON_NEUTRAL,
                UiText.StringResource(R.string.leave_now).asString(context = context)
            ) { _, _ ->
                onDateTimeSelected(currentDateTime, true)
            }
        }.show()

    }, currentDateTime.year, currentDateTime.monthValue - 1, currentDateTime.dayOfMonth).apply {
        setButton(
            DatePickerDialog.BUTTON_NEUTRAL,
            UiText.StringResource(R.string.leave_now).asString(context = context)
        ) { _, _ ->
            onDateTimeSelected(currentDateTime, true)
        }
    }.show()
}