package com.yariv.ofek.taxicounter.calculation.compose

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.yariv.ofek.taxicounter.R
import com.yariv.ofek.taxicounter.other.util.UiText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TimePickerRow(
    context: Context,
    dateTime: LocalDateTime,
    onDateTimeSelected: (LocalDateTime) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDateTimePicker(
                    context = context,
                    currentDateTime = dateTime,
                    onDateTimeSelected = onDateTimeSelected
                )
            }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_clock),
            contentDescription = "Pick Date and Time",
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = getDateTimeText(dateTime = dateTime),
        )
    }
}

@Composable
fun getDateTimeText(dateTime: LocalDateTime): String {
    return if (dateTime.isBefore(LocalDateTime.now())) {
        UiText.StringResource(R.string.leave_now).asString()
    } else {
        dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    }
}

fun showDateTimePicker(
    context: Context,
    currentDateTime: LocalDateTime,
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    DatePickerDialog(context, { _, year, month, dayOfMonth ->
        TimePickerDialog(context, { _, hour, minute ->
            onDateTimeSelected(LocalDateTime.of(year, month + 1, dayOfMonth, hour, minute))
        }, currentDateTime.hour, currentDateTime.minute, true).show()
    }, currentDateTime.year, currentDateTime.monthValue - 1, currentDateTime.dayOfMonth).apply {
        setButton(
            DatePickerDialog.BUTTON_NEUTRAL,
            UiText.StringResource(R.string.leave_now).asString(context = context)
        ) { _, _ ->
            onDateTimeSelected(LocalDateTime.now())
        }
    }.show()
}