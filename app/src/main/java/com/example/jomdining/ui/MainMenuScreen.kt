package com.example.jomdining.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jomdining.R
import com.example.jomdining.ui.theme.mainBlue
import com.example.jomdining.ui.theme.mainGreen
import com.example.jomdining.ui.theme.mainRed
import com.example.jomdining.ui.theme.mainYellow
import com.example.jomdining.ui.viewmodels.JomDiningSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    viewModel: JomDiningSharedViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFCEDFFF)
) {
    val activeLoginAccount by viewModel.activeLoginAccount.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.main_menu)) },
                actions = {
                    activeLoginAccount?.let { account ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "User",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = account.accountUsername + " [ID ${account.accountID}]",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    viewModel.logout()
                                    navController.navigate("login")
                                }
                                .padding(end = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Logout",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = backgroundColor,
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally) // Adjust horizontal spacing here
                ) {
                    ButtonBox(navController,
                        stringResource(R.string.food_ordering), "food_ordering", mainRed)
                    ButtonBox(navController,
                        stringResource(R.string.order_history), "order_history", mainBlue)
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally) // Adjust horizontal spacing here
                ) {
                    ButtonBox(navController,
                        stringResource(R.string.track_all_orders), "order_tracking", mainGreen)
                    ButtonBox(navController,
                        stringResource(R.string.menu_management), "menu_management", mainYellow)
                }
            }
        }
    }
}

@Composable
fun ButtonBox(
    navController: NavHostController,
    text: String,
    route: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { navController.navigate(route) },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = modifier
            .width(325.dp)
            .height(150.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = Color.White,
            )
        }
    }
}