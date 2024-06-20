package com.example.jomdining.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingModuleScreen(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    // Fetch all transactions and their order items when this screen is composed
    // viewModel.getAllTransactionsBeingPrepared(...)

    Scaffold(
        topBar = {
            JomDiningTopAppBar(
                title = "Order Tracking"
            )
        },
        containerColor = Color(0xFFCEDFFF)
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(modifier = modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OrderTrackingGrid(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun OrderTrackingGrid(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFCEDFFF)
) {
    LazyRow(
        modifier = modifier.fillMaxWidth()
    ) {
        val completeTrackingList = viewModel.orderTrackingUi.completeTrackingList
        items(completeTrackingList) { completeItem ->
            TransactionCard(completeItem)
        }
    }
}

@Composable
fun TransactionCard(
    completeTransaction: Pair<Transactions, List<Pair<OrderItem, Menu>>>
) {
    val transaction = completeTransaction.first
    val orderItemsList = completeTransaction.second

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .width(500.dp)
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.width(100.dp)
            ) {
                Text(text = "Transaction ID ")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${transaction.transactionID}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Column(modifier = Modifier.width(100.dp)) { Text(text = "Date ") }
            Spacer(modifier = Modifier.width(16.dp))
            Column { Text(text = transaction.transactionDateTime, fontSize = 20.sp) }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Column(modifier = Modifier.width(100.dp)) { Text(text = "Sent by") }
            Spacer(modifier = Modifier.width(16.dp))
            Column { Text(text = "${transaction.accountID}", fontSize = 20.sp) }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OrderItemListDisplay(transaction.transactionID, orderItemsList)
    }
}

@Composable
fun OrderItemListDisplay(
    connectedTransactionID: Int,
    orderItemsList: List<Pair<OrderItem, Menu>>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(orderItemsList) { pair ->
            OrderItemListBox(pair, true)
        }
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(

                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                ) {
                    Text(text = "COMPLETE ORDER", fontSize = 24.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
fun OrderItemListBox(
    orderItemAndMenu: Pair<OrderItem, Menu>,
    isCooked: Boolean
) {
    val orderItem = orderItemAndMenu.first
    val correspondingMenuItem = orderItemAndMenu.second

    var cooked by remember { mutableStateOf(isCooked) }

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth(0.15f)) {
                Text(text = "x ${orderItem.orderItemQuantity}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(text = correspondingMenuItem.menuItemName, fontSize = 20.sp)
            }
            Column(modifier = Modifier.fillMaxWidth(0.10f)) {
                Switch(
                    checked = cooked,
                    onCheckedChange = { cooked = it }
                )
            }
        }
    }
}