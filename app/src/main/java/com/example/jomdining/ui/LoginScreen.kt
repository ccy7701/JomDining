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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jomdining.R
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.jomdining.ui.theme.navRed
import com.example.jomdining.ui.theme.secondaryContainerLight
import com.example.jomdining.ui.viewmodels.JomDiningSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: JomDiningSharedViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeLoginAccount by viewModel.activeLoginAccount.observeAsState()
    val loginAttempted by viewModel.loginAttempted.observeAsState(false)
    var loginUsername by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    val errorMessage by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(secondaryContainerLight)
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures { keyboardController?.hide() }
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
            text = stringResource(R.string.login),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        TextField(
            value = loginUsername,
            onValueChange = { loginUsername = it },
            label = { Text(stringResource(R.string.username)) },
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
            onClick = { viewModel.getAccountByLoginDetails(loginUsername, loginPassword) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(300.dp)
                .height(65.dp)
        ) {
            Text(
                text = stringResource(R.string.login),
                fontSize = 25.sp
            )
        }

        // Observe login result
        LaunchedEffect(activeLoginAccount, loginAttempted) {
            if (loginAttempted) {
                if (activeLoginAccount != null) {
                    navController.navigate("main_menu") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                    Log.d("LoginScreen", "Login successful.")
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
            append(stringResource(R.string.new_user))
            withStyle(style = SpanStyle(color = navRed, fontWeight = FontWeight.Bold)) {
                append(stringResource(R.string.register_here))
                addStringAnnotation(
                    tag = "REGISTER",
                    annotation = "register",
                    start = this@buildAnnotatedString.length - (stringResource(R.string.register_here)).length,
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
