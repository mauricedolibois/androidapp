package pt.iade.games.iaderadio.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import com.example.compose.tertiaryContainerLightMediumContrast

@Composable
fun InputField(
    placeholder: String = "",
    value: TextFieldValue,
    modifier: Modifier,
    onValueChange: (TextFieldValue) -> Unit,
    onSubmit: (String) -> Unit // Function to be called with the input text when Enter is pressed
) {
    var isError by remember { mutableStateOf(false) }

    AppTheme {
        Column (modifier = modifier) {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                    isError = isError && it.text.length == 5
                },
                placeholder = {
                    if (value.text.isEmpty()) {
                        Text(text = placeholder) // Show placeholder when there is no text
                    } else {
                        Text(text = value.text) // Show current input text when available
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tertiaryContainerLightMediumContrast, shape = RoundedCornerShape(50.dp)),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // Specify the action to be "Done" or "Enter"
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (value.text.length == 5) {
                            onSubmit(value.text) // Pass the current input text to onSubmit
                            onValueChange(TextFieldValue("")) // Clear the input field after submitting
                            isError = false
                        } else {
                            isError = true
                        }
                    }
                ),
                isError = isError
            )
            if (isError) {
                Text(
                    text = "Code must be 5 characters long",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun InputFieldPreview() {
    var textState by remember { mutableStateOf(TextFieldValue("")) } // Manage the state for the input field
    var submittedText by remember { mutableStateOf("") } // Store the submitted text

    InputField(
        placeholder = "ENTER GAME CODE",
        modifier = Modifier,
        value = textState,
        onValueChange = { textState = it },
        onSubmit = { inputText ->
            submittedText = inputText
            Log.d("InputField", "Submitted text: $submittedText")
        }
    )
}