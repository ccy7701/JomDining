package com.example.jomdining.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.jomdining.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val errorMessage by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFCEDFFF))
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    // Hide the keyboard when tapped outside
                    keyboardController?.hide()
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.register),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.username)) },
            modifier = Modifier
                .width(800.dp)
                .padding(bottom = 16.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier
                .width(800.dp)
                .padding(bottom = 16.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .width(800.dp)
                .padding(bottom = 16.dp)
        )
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(R.string.confirm_password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .width(800.dp)
                .padding(bottom = 16.dp)
        )
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(35.dp))
        Button(
            onClick = {
                // First, check if password and confirmPassword are equal
                if (password == confirmPassword) {
                    try {
                        // Push this new account information to the DB
                        Log.d("RegAttempt", "Now attempting with $username, $password, $email")
                        viewModel.registerAndCreateNewAccount(
                            accountUsername = username,
                            accountPassword = password,
                            accountEmail = email
                        )
                        Toast
                            .makeText(
                                context,
                                "New account registered successfully!",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        // Then navigate back to the login menu
                        navController.navigate("login")
                    } catch (e: Exception) {
                        Log.d("AccountRegistration", "Error when registering new account: $e")
                    }
                } else {
                    Toast
                        .makeText(
                            context,
                            "Password and confirm password fields do not match. Please try again.",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(300.dp)
                .height(65.dp)
        ) {
            Text(
                text = stringResource(R.string.register),
                fontSize = 25.sp
            )
        }
    }
}
