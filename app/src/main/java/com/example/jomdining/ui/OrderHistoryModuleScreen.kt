package com.example.jomdining.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryModuleScreen(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    // Fetch current order history list when this screen is composed
    val activeLoginAccount by viewModel.activeLoginAccount.observeAsState()
    LaunchedEffect(activeLoginAccount) {
        activeLoginAccount?.let { account ->
            account.accountID.let { accountID ->
                // Fetch order history when this screen is composed
                viewModel.getAllHistoricalTransactions(accountID)
            }
        }
    }

    Scaffold(
        topBar = {
            JomDiningTopAppBar(title = "JomDining")
        },
        containerColor = Color(0xFFCEDFFF)
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectTapGestures {
                        keyboardController?.hide()
                    }
                }
        ) {
            Row(
                modifier = modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OrderHistoryDetailsCard(viewModel = viewModel)
                }
                OrderHistoryList(
                    viewModel = viewModel,
                    navController = navController,
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun OrderHistoryDetailsCard(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Order Details",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                )
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                Text(
                    text = "***All relevant order details are shown here***",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall.copy(fontStyle = FontStyle.Italic),
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34197C)),
                ) {
                    Text(
                        text = "Edit Order",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        fontSize = 18.sp
                    )
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text(
                        text = "Delete Order",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryList(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFFE6E6E6))
            .padding(16.dp)
    ) {
        OrderHistoryListCard(viewModel = viewModel, navController = navController, content1 = "Order No.", content2 = "Transaction ID", content3 = "Date & Time")
        OrderHistoryListCard(viewModel = viewModel, navController = navController, content1 = "Order No.", content2 = "Transaction ID", content3 = "Date & Time")
    }
}

@Composable
fun OrderHistoryListCard(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    content1: String,
    content2: String,
    content3: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
    ) {
        Text(
            text = content1 + "\n" + content2 + "\n" + content3,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.Black,
            textAlign = TextAlign.Start
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}