package com.example.jomdining.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.jomdining.R
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions
import com.example.jomdining.ui.components.JomDiningTopAppBar
import java.util.Locale

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
            JomDiningTopAppBar(
                title = "Order History",
                onBackClicked = { navController.popBackStack() }
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
                    OrderHistoryDetailsDisplay(viewModel = viewModel)
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
fun OrderHistoryDetailsDisplay(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier
) {
    val transactionToDisplay by viewModel.activeHistoricalTransaction.observeAsState()
    val currentHistoricalOrderItemsList = viewModel.orderHistoryOrderItemsUi.orderHistoryOrderItemsList

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.padding(16.dp).fillMaxSize(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (viewModel.transactionIsSelected == 1) White else LightGray
        )
    ) {
        if (viewModel.transactionIsSelected == 1) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier.clickable {
                                viewModel.transactionIsSelected = 0
                                viewModel.selectedTransactionID = 0
                            }
                        ){
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    Text(
                        text = "TransactionID: ${transactionToDisplay!!.transactionID}",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Table Number: ${transactionToDisplay!!.tableNumber}",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    Text(
                        text = "Date and Time: ${transactionToDisplay!!.transactionDateTime}",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        modifier = modifier.weight(1f).padding(horizontal = 16.dp)
                    ) {
                        items(currentHistoricalOrderItemsList) { pair ->
                            val orderItem = pair.first
                            val correspondingMenuItem = pair.second
                            PastOrderItemCard(orderItemAndMenu = Pair(orderItem, correspondingMenuItem))
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.weight(0.8f)
                                ) {
                                    Text(
                                        text = "TOTAL: RM\t",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.weight(0.2f)
                                ) {
                                    Text(
                                        text = String.format(Locale.getDefault(), "%.2f", transactionToDisplay!!.transactionTotalPrice),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.weight(0.8f)
                                ) {
                                    Text(text = "Paid: RM\t")
                                }
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.weight(0.2f)
                                ) {
                                    Text(text = String.format(Locale.getDefault(), "%.2f", transactionToDisplay!!.transactionPayment))
                                }
                            }
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.weight(0.8f)
                                ) {
                                    Text(text = "Change: RM\t")
                                }
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.weight(0.2f)
                                ) {
                                    Text(text = String.format(Locale.getDefault(), "%.2f", transactionToDisplay!!.transactionBalance))
                                }
                            }
                            Column(horizontalAlignment = Alignment.Start) {
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                    Text(text = "Paid by: " + transactionToDisplay!!.transactionMethod)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.no_item_chosen), color = Color.Gray, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun OrderHistoryList(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .background(Color(0xFFE6E6E6))
            .padding(16.dp)
    ) {
        items(viewModel.orderHistoryUi.orderHistoryList) { orderHistoryItem ->
            OrderHistoryListCard(
                viewModel = viewModel,
                transactionsObject = orderHistoryItem,
                modifier = Modifier.padding(16.dp)
//                navController = navController,
            )
        }
    }
}

@Composable
fun OrderHistoryListCard(
    viewModel: JomDiningViewModel,
    transactionsObject: Transactions,
//    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(White, shape = RoundedCornerShape(8.dp))
            .clickable {
                viewModel.transactionIsSelected = 1
                viewModel.getHistoricalTransactionDetailsByID(transactionsObject.transactionID)
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            if (transactionsObject.isActive == 0) {  // transaction food not yet completed
                Text(text = "Preparing", color = Color(0xFFA9A9A9), fontSize = 20.sp)
            } else if (transactionsObject.isActive == -1) {
                Text(text = "Completed", color = Color(0xFF50C878), fontSize = 20.sp)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(text = "Transaction ID: ${transactionsObject.transactionID}", fontSize = 20.sp)
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            Text(text = "Transaction Date: ${transactionsObject.transactionDateTime}", fontSize = 20.sp)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(text = String.format(Locale.getDefault(), "TOTAL: RM%.2f", transactionsObject.transactionTotalPrice), fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}
@Composable
fun PastOrderItemCard(
    orderItemAndMenu: Pair<OrderItem, Menu>,
    modifier: Modifier = Modifier
) {
    val orderItem = orderItemAndMenu.first
    val correspondingMenuItem = orderItemAndMenu.second

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = modifier
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
                Text(
                    text = correspondingMenuItem.menuItemName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = String.format(Locale.getDefault(), "RM %.2f", correspondingMenuItem.menuItemPrice),
                    color = Color(0xFF7C4DFF)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier.width(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format(Locale.getDefault(), "x %s", orderItem.orderItemQuantity.toString()),
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    modifier = Modifier.width(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val orderItemCost = orderItem.orderItemQuantity * correspondingMenuItem.menuItemPrice
                    Text(
                        text = String.format(Locale.getDefault(), "RM %.2f", orderItemCost)
                    )
                }
            }
        }
    }
}