package com.example.jomdining.ui

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.jomdining.R
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions
import com.example.jomdining.ui.components.JomDiningTopAppBar
import com.example.jomdining.ui.theme.secondaryContainerLight
import com.example.jomdining.ui.viewmodels.JomDiningViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingModuleScreen(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // Fetch all transactions and their order items when this screen is composed
    viewModel.getAllTransactionsBeingPrepared()

    Scaffold(
        topBar = {
            JomDiningTopAppBar(
                title = stringResource(R.string.order_tracking),
                onBackClicked = { navController.popBackStack() }
            )
        },
        containerColor = secondaryContainerLight
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
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth()
    ) {
        val completeTrackingList = viewModel.orderTrackingUi.completeTrackingList
        items(completeTrackingList) { completeItem ->
            TransactionCard(viewModel, completeItem)
        }
    }
}

@Composable
fun TransactionCard(
    viewModel: JomDiningViewModel,
    completeTransaction: Pair<Transactions, List<Pair<OrderItem, Menu>>>
) {
    val transaction = completeTransaction.first
    val orderItemsList = completeTransaction.second

    Card(
        colors = CardDefaults.cardColors(containerColor = White),
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
            Column(modifier = Modifier.width(100.dp)) { Text(text = "Sender ID") }
            Spacer(modifier = Modifier.width(16.dp))
            Column { Text(text = "${transaction.accountID}", fontSize = 20.sp) }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OrderItemListDisplay(viewModel, transaction.transactionID, orderItemsList)
    }
}

@Composable
fun OrderItemListDisplay(
    viewModel: JomDiningViewModel,
    connectedTransactionID: Int,
    orderItemsList: List<Pair<OrderItem, Menu>>
) {
    val context = LocalContext.current

    // State to track if all items in this list have been served
    val allOrderItemsServed = remember { mutableStateOf(false) }

    // Update allOrderItemsServed based on orderItemsList
    LaunchedEffect(orderItemsList) {
        allOrderItemsServed.value = orderItemsList.all { it.first.foodServed == 1 }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(orderItemsList) { pair ->
            OrderItemListBox(viewModel, connectedTransactionID, pair)
        }
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                var showCompleteConfirmationDialog by remember { mutableStateOf(false) }
                Button(
                    onClick = { showCompleteConfirmationDialog = true },
                    colors = ButtonDefaults.buttonColors(),
                    enabled = allOrderItemsServed.value,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                ) {
                    Text(text = stringResource(R.string.complete_order), fontSize = 24.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
                // Confirmation dialog
                if (showCompleteConfirmationDialog) {
                    AlertDialog(
                        onDismissRequest = { showCompleteConfirmationDialog = false },
                        title = { Text(text = "Confirm transaction completion") },
                        text = { Text(text = "Mark this transaction as complete? This action cannot be undone!") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    // Invoke the completion function from the viewmodel
                                    viewModel.updateTransactionAsCompleted(connectedTransactionID)
                                    // Display a Toast message indicating the process has passed
                                    Toast
                                        .makeText(
                                            context,
                                            "Transaction with ID $connectedTransactionID marked as completed.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    // Hide the dialog
                                    showCompleteConfirmationDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Red)
                            ) {
                                Text(text = "Confirm", color = White)
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showCompleteConfirmationDialog = false }
                            ) {
                                Text(text = "Cancel")
                            }
                        },
                        properties = DialogProperties(dismissOnClickOutside = true)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderItemListBox(
    viewModel: JomDiningViewModel,
    connectedTransactionID: Int,
    orderItemAndMenu: Pair<OrderItem, Menu>,
) {
    val orderItem = orderItemAndMenu.first
    val correspondingMenuItem = orderItemAndMenu.second

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
                    checked = when (orderItem.foodServed) {
                        1 -> true
                        else -> false
                    },
                    onCheckedChange = { foodServed ->
                        val newFlag = if (foodServed) 1 else 0
                        Log.d("NewFlag", "New flag after the backend is complete --> $newFlag")
                        Log.d("test", "Will update for TrxID $connectedTransactionID, orderItem with menuItemID ${orderItem.menuItemID} and flag $newFlag")
                        viewModel.updateFoodServedFlag(newFlag, connectedTransactionID, orderItem.menuItemID)
                    }
                )
            }
        }
    }
}