package com.yariv.ofek.taxicounter.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.yariv.ofek.taxicounter.presentation.calculation.compose.CustomOutlinedTextField
import org.junit.Rule
import org.junit.Test

class CustomOutlinedTextFieldTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testIconSwitchingBehavior() {
        // Set up the test content
        composeTestRule.setContent {
            var text by remember { mutableStateOf("") }
            CustomOutlinedTextField(
                state = text,
                onValueChange = { text = it },
                label = "Test Field",
                suggestions = emptyList(),
                onSuggestionSelected = {},
                focusRequester = null,
            )
        }

        // Assert that the microphone icon is initially displayed
        composeTestRule.onNodeWithTag("micIcon").assertExists()

        // Enter text in the field
        composeTestRule.onNodeWithText("Test Field").performTextInput("Hello")

        // Assert that the clear icon is displayed
        composeTestRule.onNodeWithTag("clearIcon").assertExists()

        // Assert that the microphone icon is no longer displayed
        composeTestRule.onNodeWithTag("micIcon").assertDoesNotExist()

        // Click the clear icon
        composeTestRule.onNodeWithTag("clearIcon").performClick()

        // Assert that the text is cleared and the microphone icon reappears
        composeTestRule.onNodeWithText("Hello").assertDoesNotExist()
        composeTestRule.onNodeWithTag("micIcon").assertExists()
    }
}

