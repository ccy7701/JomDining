package com.example.jomdining.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jomdining.R
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeLoginAccount by viewModel.activeLoginAccount.observeAsState()
    val loginAttempted by viewModel.loginAttempted.observeAsState(false)
    var loginUsername by remember { mutableStateOf(TextFieldValue("")) }
    var loginPassword by remember { mutableStateOf(TextFieldValue("")) }
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
        Image(
            painter = painterResource(id = R.drawable.jomdininglogo),
            contentDescription = "JomDining Logo",
            modifier = Modifier
                .height(220.dp)
                .width(220.dp)
                .padding(bottom = 12.dp)
        )
        Text(
            text = "Login",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        TextField(
            value = loginUsername,
            onValueChange = { loginUsername = it },
            label = { Text("Username") },
            modifier = Modifier
                .width(800.dp)
                .padding(bottom = 16.dp)
        )
        TextField(
            value = loginPassword,
            onValueChange = { loginPassword = it },
            label = { Text(stringResource(R.string.password)) },
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
                Log.d("LoginScreen", "Attempting login with username: ${loginUsername.text}")
                viewModel.getAccountByLoginDetails(loginUsername.text, loginPassword.text)
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(300.dp)
                .height(65.dp)
        ) {
            Text(
                text = "Login",
                fontSize = 25.sp
            )
        }

        // Observe login result
        LaunchedEffect(activeLoginAccount, loginAttempted) {
            if (loginAttempted) {
                if (activeLoginAccount != null) {
//                    Log.d("LoginScreen", "Login successful: ${activeLoginAccount!!.accountID}")
                    navController.navigate("main_menu") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                    Log.d("LoginScreen", "Login successful. The system currently holds the following session details:")
                    Log.d("SessionDetails", "$activeLoginAccount")
                } else {
                    Log.d("LoginScreen", "Login failed: No matching account found.")
                    // If login fails, display the Toast message indicating it.
                    Toast
                        .makeText(
                            context,
                            "Login failed. Please try again.",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                viewModel.resetLoginAttempt()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        val annotatedString = buildAnnotatedString {
            append("New user? ")
            withStyle(style = SpanStyle(color = Color(0xFF7d0000), fontWeight = FontWeight.Bold)) {
                append("Register here")
                addStringAnnotation(
                    tag = "REGISTER",
                    annotation = "register",
                    start = this@buildAnnotatedString.length - "Register here".length,
                    end = this@buildAnnotatedString.length
                )
            }
        }

        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "REGISTER", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.navigate("register")
                    }
            },
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    val navController = rememberNavController()
//    LoginScreen(navController = navController)
//}
