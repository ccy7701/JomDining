package com.example.jomdining.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.jomdining.R
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodOrderingModuleScreen(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    // Fetch menu items when this screen is composed
    viewModel.getAllMenuItems()

    // Then, fetch current order items list when this screen is composed
    val activeLoginAccount by viewModel.activeLoginAccount.observeAsState()
    LaunchedEffect(activeLoginAccount) {
        activeLoginAccount?.let { account ->
            account.accountID.let { accountID ->
                viewModel.getCurrentActiveTransaction(accountID)
            }
        }
    }
    val activeTransaction by viewModel.activeTransaction.observeAsState()

    Scaffold(
        topBar = {
            JomDiningTopAppBar(
                title = "Food Ordering"
            )
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
                    if (activeTransaction != null) {
                        MenuItemGrid(
                            viewModel = viewModel,
                            currentActiveTransactionID = activeTransaction!!.transactionID,
                            modifier = modifier
                        )
                    } else {
                        Text("Loading transaction...")
                    }
                }
                OrderSummary(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JomDiningTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(title)
        },
        modifier = modifier,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun MenuItemGrid(
    viewModel: JomDiningViewModel,
    currentActiveTransactionID: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFCEDFFF)
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .background(backgroundColor)
    ) {
        items(viewModel.menuUi.menuItems) { menuItem ->
            MenuItemCard(viewModel, currentActiveTransactionID, menuItem)
        }
    }
}

@Composable
fun MenuItemCard(
    viewModel: JomDiningViewModel,
    currentActiveTransactionID: Int,
    menuItem: Menu,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(16.dp)
            .clickable {
                viewModel.addNewOrIncrementOrderItem(
                    transactionID = currentActiveTransactionID,
                    menuItemID = menuItem.menuItemID,
                    operationFlag = 1
                )
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                val imagePath = menuItem.menuItemImagePath
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("file:///android_asset/images/menu/$imagePath")
                            .build()
                    ),
                    contentDescription = menuItem.menuItemName,
                    modifier = modifier
                        .padding(bottom = 4.dp)
                        .size(width = 120.dp, height = 120.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Row {
                Text(
                    text = stringResource(
                        R.string.menu_item_name_placeholder,
                        menuItem.menuItemName
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 8.dp
                    )
                )
            }
            Row {
                Text(
                    text = String.format(Locale.getDefault(), "RM %.2f", menuItem.menuItemPrice),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(74, 22, 136)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummary(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val activeTransaction by viewModel.activeTransaction.observeAsState()
    val currentActiveTransactionList = viewModel.transactionsUi.currentActiveTransactionList

    var runningTotal by remember { mutableDoubleStateOf(0.0) }
    // calculate runningTotal when currentOrderItemsList changes
    val currentOrderItemsList = viewModel.orderItemUi.orderItemsList
    LaunchedEffect(currentOrderItemsList) {
        runningTotal = currentOrderItemsList.sumOf{ (orderItem, menu) ->
            orderItem.orderItemQuantity * menu.menuItemPrice
        }
    }
    var totalPaymentAmountString by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("-") }
    var tableNumber by remember { mutableStateOf("-") }

    if (currentActiveTransactionList.isNotEmpty()) {
        // val currentOrderItemsList = viewModel.orderItemUi.orderItemsList
        Column(
            modifier = modifier
                .background(Color(0xFFE6E6E6))
                .padding(16.dp)
        ) {
            Text(
                text = "Order ${currentActiveTransactionList.elementAt(0).transactionID}",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF34197C), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.weight(1f) // Make it take available space and be scrollable
            ) {
                items(currentOrderItemsList) { pair ->
                    val orderItem = pair.first
                    val correspondingMenuItem = pair.second
                    OrderItemCard(
                        viewModel = viewModel,
                        orderItemAndMenu = Pair(orderItem, correspondingMenuItem)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = String.format(Locale.getDefault(), "%.2f", runningTotal),
                    onValueChange = {},
                    label = { Text(stringResource(R.string.total)) },
                    readOnly = true,
                    singleLine = true,
                    leadingIcon = { Text("RM ") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = String.format(Locale.getDefault(), "%s", totalPaymentAmountString),
                    onValueChange = { newAmount ->
                        totalPaymentAmountString = newAmount
                    },
                    label = { Text(stringResource(R.string.total_payment_amount)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    leadingIcon = { Text("RM ") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.table_number),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                var tableNumberDropdownExpanded by remember { mutableStateOf(false) }
                val allTableNumbers = (1..10).map { it.toString() }
                Box(
                    modifier = Modifier
                        .width(144.dp)
                        .height(48.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .clickable { tableNumberDropdownExpanded = true },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(text = tableNumber, modifier = Modifier.padding(end = 8.dp))
                    DropdownMenu(
                        expanded = tableNumberDropdownExpanded,
                        onDismissRequest = { tableNumberDropdownExpanded = false },
                        modifier = Modifier
                            .width(144.dp)
                            .padding(top = 4.dp)
                    ) {
                        allTableNumbers.forEach { number ->
                            DropdownMenuItem(
                                text = { Text(number) },
                                onClick = {
                                    tableNumber = number
                                    tableNumberDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.payment_method),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                var paymentMethodDropdownExpanded by remember { mutableStateOf(false) }
                val allPaymentMethods = listOf("Cash", "Card", "EWallet")
                Box(
                    modifier = Modifier
                        .width(144.dp)
                        .height(48.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .clickable { paymentMethodDropdownExpanded = true },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(text = paymentMethod, modifier = Modifier.padding(end = 8.dp))
                    DropdownMenu(
                        expanded = paymentMethodDropdownExpanded,
                        onDismissRequest = { paymentMethodDropdownExpanded = false },
                        modifier = Modifier
                            .width(144.dp)
                            .padding(top = 4.dp)
                    ) {
                        allPaymentMethods.forEach { method ->
                            DropdownMenuItem(
                                text = { Text(method) },
                                onClick = {
                                    paymentMethod = method
                                    paymentMethodDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly // Center the buttons horizontally
            ) {
                Column {
                    var showResetConfirmationDialog by remember { mutableStateOf(false) }
                    Button(
                        onClick = {
                            if (currentOrderItemsList.isEmpty()) {
                                Toast
                                    .makeText(
                                        context,
                                        "The order list is currently empty.",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                showResetConfirmationDialog = false
                                return@Button
                            } else {
                                showResetConfirmationDialog = true
                            }
                        },
                        modifier = Modifier.height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C))
                    ) {
                        Text(
                            "Reset",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }
                    // Confirmation Dialog
                    if (showResetConfirmationDialog) {
                        AlertDialog(
                            onDismissRequest = { showResetConfirmationDialog = false },
                            title = { Text(text = "Confirm Cancellation") },
                            text = { Text(text = "Are you sure you want to cancel?") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        currentOrderItemsList.forEach { (orderItem, _) ->
                                            Log.d("OrderItemID", "${orderItem.menuItemID}")
                                            val currentTransactionID = orderItem.transactionID
                                            val currentMenuItemID = orderItem.menuItemID
                                            viewModel.deleteOrDecrementOrderItem(
                                                transactionID = currentTransactionID,
                                                menuItemID = currentMenuItemID,
                                                operationFlag = 1
                                            )
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Order cancelled and order list cleared.",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                        totalPaymentAmountString = ""
                                        tableNumber = "-"
                                        paymentMethod = "-"
                                        showResetConfirmationDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Red)
                                ) {
                                    Text(text = "Yes")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        showResetConfirmationDialog = false
                                        Log.d("cancelOrder", "Order cancelled.")
                                    }
                                ) {
                                    Text(text = "No")
                                }
                            },
                            properties = DialogProperties(dismissOnClickOutside = true)
                        )
                    }
                }
                Column {
                    Button(
                        onClick = {
                            try {
                                // collect all the variables that will be pushed to the DB
                                val pushTransactionID = activeTransaction?.transactionID
                                val pushAccountID = activeTransaction?.accountID
                                val pushDateTime = getCurrentDateTime()
                                var pushPaymentMethod: String? = null
                                val pushTransactionTotalPrice = runningTotal
                                val pushTransactionPayment = totalPaymentAmountString.toDouble()
                                val pushTransactionBalance = String.format(Locale.getDefault(), "%.2f", pushTransactionPayment - pushTransactionTotalPrice).toDouble()
                                var pushTableNumber: Int? = null

                                // The data has to go through all four checks and pass them all before pushing to DB
                                if (currentOrderItemsList.isEmpty()) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Can't confirm order with an empty order list",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    return@Button
                                }
                                if (paymentMethod == "-") {
                                    Toast
                                        .makeText(
                                            context,
                                            "Please make sure to select a payment method.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    return@Button
                                }
                                if (tableNumber == "-") {
                                    Toast
                                        .makeText(
                                            context,
                                            "Please make sure to select a table number.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    return@Button
                                }
                                if (pushTransactionPayment < pushTransactionTotalPrice) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Total payment amount not sufficient. Please check again.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    return@Button
                                }

                                pushPaymentMethod = paymentMethod
                                pushTableNumber = tableNumber.toInt()

                                // If everything passes, the push to DB should be successful
                                Log.d("ConfirmOrder", "DB push expected with the following values.")
                                Log.d("ConfirmOrder", "$pushTransactionID, $pushAccountID, $pushDateTime, $pushPaymentMethod, $pushTransactionTotalPrice, $pushTransactionPayment, $pushTransactionBalance, $pushTableNumber, isActive -> 0")

                                Toast
                                    .makeText(
                                        context,
                                        "Order successfully placed!",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                navController.navigate("main_menu")
                                // THE ACTUAL DB BACKEND IS NOT DONE YET!
                            } catch (e: Exception) {
                                Log.e("ConfirmOrder", "Error: $e. Check your input again.")
                                Toast
                                    .makeText(
                                        context,
                                        "Please check the Total Payment Amount input again.",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                        modifier = Modifier.height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34197C))
                    ) {
                        Text(
                            stringResource(R.string.confirm_order),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(
    viewModel: JomDiningViewModel,
    orderItemAndMenu: Pair<OrderItem, Menu>,
    modifier: Modifier = Modifier
) {
    // Log.d("CMP_OrderItemCard", "Composable function invoked. Details: $orderItemAndMenu")
    val currentOrderItem = orderItemAndMenu.first
    val correspondingMenuItem = orderItemAndMenu.second
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val imagePath = correspondingMenuItem.menuItemImagePath
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/images/menu/$imagePath")
                        .build()
                ),
                contentDescription = "Ordered Item: ${correspondingMenuItem.menuItemName}",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                val currentOrderItemCost = currentOrderItem.orderItemQuantity * correspondingMenuItem.menuItemPrice
                Text(
                    text = correspondingMenuItem.menuItemName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = String.format(Locale.getDefault(), "RM %.2f", currentOrderItemCost),
                    color = Color(0xFF7C4DFF)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Red)
                        .clickable {
                            // If the orderItemQuantity already is at 1, display the Toast message indicating it.
                            if (currentOrderItem.orderItemQuantity == 1) {
                                Toast
                                    .makeText(
                                        context,
                                        "Order item quantity already at 1, cannot decrease further!",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                            // Proceed as normal otherwise.
                            viewModel.deleteOrDecrementOrderItem(
                                transactionID = currentOrderItem.transactionID,
                                menuItemID = currentOrderItem.menuItemID,
                                operationFlag = 2
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease Quantity",
                        tint = Color.White
                    )
                }
                Box(
                    modifier = Modifier.width(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentOrderItem.orderItemQuantity.toString(),
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Green)
                        .clickable {
                            viewModel.addNewOrIncrementOrderItem(
                                transactionID = currentOrderItem.transactionID,
                                menuItemID = currentOrderItem.menuItemID,
                                operationFlag = 2
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Quantity",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {
                    viewModel.deleteOrDecrementOrderItem(
                        transactionID = currentOrderItem.transactionID,
                        menuItemID = currentOrderItem.menuItemID,
                        operationFlag = 1
                    )
                    Toast.makeText(context, "${correspondingMenuItem.menuItemName} removed from order", Toast.LENGTH_SHORT).show()
                } ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Item",
                        tint = Red
                    )
                }
            }
        }
    }
}

fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kuala_Lumpur")
    return dateFormat.format(Date())
}