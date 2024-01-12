package com.yariv.ofek.taxicounter.calculation.compose

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.yariv.ofek.taxicounter.R
import com.yariv.ofek.taxicounter.other.util.UiText

@Composable
fun CustomOutlinedTextField(
    state: String,
    onValueChange: (String) -> Unit,
    label: String,
    suggestions: List<String>,
    onSuggestionSelected: (String) -> Unit,
    focusRequester: FocusRequester? = null,
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            singleLine = true,
            textStyle = TextStyle(textDirection = TextDirection.Content),
            value = state,
            onValueChange = {
                onValueChange(it)
                isDropdownExpanded = it.isNotEmpty()
            },
            label = { Text(text = label) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester ?: FocusRequester()),
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                if (state.isNotEmpty()) {
                    IconButton(
                        onClick = { onValueChange("") },
                        modifier = Modifier.testTag("clearIcon")
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                            contentDescription = label,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    SpeechToText(onResult = { result -> onValueChange(result) })
                }
            }
        )

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            properties = PopupProperties(focusable = false),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(
                    onClick = {
                        onSuggestionSelected(suggestion)
                        isDropdownExpanded = false
                    },
                    text = { Text(text = suggestion) }
                )
            }
        }
    }

}

@Composable
fun SpeechToText(onResult: (String) -> Unit) {
    val context = LocalContext.current
    val startForResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val data = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!data.isNullOrEmpty()) {
                onResult(data[0])
            }
        }
    }

    IconButton(
        modifier = Modifier.testTag("micIcon"),
        onClick = {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "he-IL,en-US")
                putExtra(
                    RecognizerIntent.EXTRA_PROMPT,
                    UiText.StringResource(R.string.speak_now).asString(context)
                )
            }
            startForResult.launch(intent)
        }) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_mic),
            contentDescription = "Speech to text"
        )
    }
}