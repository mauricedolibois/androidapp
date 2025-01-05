package pt.iade.games.iaderadio.ui.components.code

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import pt.iade.games.iaderadio.network.FuelClient
import pt.iade.games.iaderadio.ui.components.shared.IconButton

@Composable
fun InputField(
    placeholder: String = "",
    value: TextFieldValue,
    modifier: Modifier,
    onValueChange: (TextFieldValue) -> Unit,
    onSubmit: (String, Boolean) -> Unit // Function to be called with the input text and session validity
) {
    var isError by remember { mutableStateOf(false) }
    var isSessionValid by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    AppTheme {
        Column {
            Row(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                        isError = isError && it.text.length == 5
                    },
                    placeholder = {
                        if (value.text.isEmpty()) {
                            Text(text = placeholder)
                        } else {
                            Text(text = value.text)
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .weight(1f)
                        .width(200.dp)
                        .padding(start = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (value.text.length == 5) {
                                isLoading = true // Show loading state
                                validateSession(context, value.text) { isValid ->
                                    isLoading = false // Hide loading state
                                    isSessionValid = isValid
                                    if (isValid) {
                                        onSubmit(value.text, true)
                                        onValueChange(TextFieldValue("")) // Clear the input field
                                        isError = false
                                    } else {
                                        isError = true
                                    }
                                }
                            } else {
                                isError = true
                            }
                        }
                    ),
                    isError = isError
                )

                IconButton(
                    icon = Icons.Default.Check,
                    contentDescription = "Submit",
                    onClick = {
                        if (value.text.length == 5) {
                            isLoading = true
                            validateSession(context, value.text) { isValid ->
                                isLoading = false
                                isSessionValid = isValid
                                if (isValid) {
                                    onSubmit(value.text, true)
                                    onValueChange(TextFieldValue("")) // Clear the input field
                                    isError = false
                                } else {
                                    isError = true
                                }
                            }
                        } else {
                            isError = true
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            }

            if (isError) {
                Text(
                    text = if (isSessionValid) "Code must be 5 characters long" else "Invalid session code",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 4.dp, start =8.dp)
                )
            }
            if (isLoading) {
                Text(
                    text = "Validating...",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                )
            }
        }
    }
}



private fun validateSession(context:Context,code: String, onResult: (Boolean) -> Unit) {
    FuelClient.getSessionByCode(context, code) { session, error ->
        if (session != null) {
            Log.d("InputField", "Session: $session")
            FuelClient.getCurrentRoombySessionID(context, session.toInt()) { room, error ->
                if (room != null) {
                    Log.d("InputField", "Room: $room")
                    onResult(true)
                } else {
                    Log.e("InputField", "Error fetching room: $error")
                    onResult(false)
                }
            }
        } else {
            Log.e("InputField", "Error fetching session: $error")
            onResult(false)
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
        onSubmit = { inputText, isValid ->
            submittedText = inputText
            Log.d("InputField", "Submitted text: $submittedText, isValid: $isValid")
        }
    )
}