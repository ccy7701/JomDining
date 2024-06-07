package com.example.jomdining.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.jomdining.R
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodOrderingModuleScreen(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier,
) {
    // Fetch current order items list when this screen is composed
    LaunchedEffect(Unit) {
        // THIS IS CURRENTLY HARDCODED FOR TESTING!
        viewModel.getCurrentActiveTransaction(1)
    }


    Scaffold(
        topBar = {
            JomDiningTopAppBar(
                title = "JomDining"
            )
        },
        containerColor = Color(0xFFCEDFFF)
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                    MenuItemGrid(
                        viewModel = viewModel,
                        // THIS IS CURRENTLY HARDCODED FOR TESTING!
                        // currentActiveTransactionID = currentActiveTransaction.transactionID,
                        currentActiveTransactionID = 1,
                        modifier = modifier
                    )
                }
                OrderSummary(
                    viewModel = viewModel,
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
            MenuItemCard(viewModel, currentActiveTransactionID /* THIS IS CURRENTLY HARDCODED! */, menuItem)
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
                    // THIS IS CURRENTLY HARDCODED!
                    transactionID = currentActiveTransactionID,
                    menuItemID = menuItem.menuItemID,
                    operationFlag = 1
                )
            },
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
                // Log.d("IMAGE_PATH", "Current image path is $imagePath")
                val assetManager = LocalContext.current.assets
                val inputStream = try {
                    assetManager.open(imagePath)
                } catch (e: Exception) {
                    Log.e("IMAGE_FILE_ERROR", "Image file does not exist in assets: $e")
                    null
                }
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
    modifier: Modifier = Modifier
) {
    val currentActiveTransaction = viewModel.transactionsUi.currentActiveTransaction
    // Log.d("CAT_InComposableCtnt", "Content of currentActiveTransaction: $currentActiveTransaction")

    if (currentActiveTransaction.isNotEmpty()) {
        // Log.d("CAT_TestOutput", "TransactionID: ${currentActiveTransaction.elementAt(0).transactionID}")
        val currentOrderItemsList = viewModel.orderItemUi.orderItemsList

        Column(
            modifier = modifier
                .background(Color(0xFFE6E6E6))
                .padding(16.dp)
        ) {
            Text(
                text = "Order ${currentActiveTransaction.elementAt(0).transactionID}",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
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
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = String.format(Locale.getDefault(), "RM %.2f", currentActiveTransaction.elementAt(0).transactionTotalPrice),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
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
                    text = "Table No.",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                TextField(
                    value = currentActiveTransaction.elementAt(0).tableNumber.toString(),
                    onValueChange = {},
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center // Center the buttons horizontally
            ) {
                Button(
                    onClick = { /* Place Order Action */ },
                    modifier = Modifier
                        .width(300.dp)
                        .height(65.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34197C))
                ) {
                    Text(
                        "Place Order",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center // Center the buttons horizontally
            ) {
                Button(
                    onClick = { /* Cancel Order Action */ },
                    modifier = Modifier
                        .width(300.dp)
                        .height(65.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C))
                ) {
                    Text(
                        "Cancel",
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
            val assetManager = context.assets
            val inputStream =
                try {
                    assetManager.open(imagePath)
                } catch (e: Exception) {
                    Log.e("ImagePathLoadError", "Error loading image from assets: $e")
                }
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
                        .background(Color.Red)
                        .clickable {
                            // If the orderItemQuantity already is at 1, display the Toast message indicating it.
                            if (currentOrderItem.orderItemQuantity == 1) {
                                Toast.makeText(context, "Order item quantity already at 1, cannot decrease further!", Toast.LENGTH_SHORT).show()
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
                        tint = Color.Red
                    )
                }
            }
        }
    }
}


