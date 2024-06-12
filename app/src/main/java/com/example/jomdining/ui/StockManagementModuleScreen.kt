package com.example.jomdining.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.jomdining.R
import com.example.jomdining.databaseentities.Stock

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("AutoboxingStateCreation")
@Composable
fun StockManagementModuleScreen(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier
) {
    // Fetch stock items when this screen is composed
    viewModel.getAllStockItems()

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
        ) {
            Row(
                modifier = modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    StockItemGrid(viewModel, modifier)
                }
                Box(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxSize()
                        .background(Color.LightGray)
                ) {
                    StockItemActionDisplay(viewModel)
                }
            }
        }
    }
}

// Composable for the grid of stock item cards
@Composable
fun StockItemGrid(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFCEDFFF)
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.background(backgroundColor)
    ) {
        items(viewModel.stockUi.stockItems) { stockItem ->
            StockItemCard(viewModel, stockItem)
        }
        item {
            AddNewStockItemCard()
        }
    }
}

// Composable for an individual stock item card
@Composable
fun StockItemCard(
    viewModel: JomDiningViewModel,
    stockItem: Stock
) {
    Card(
        modifier = Modifier
            .width(270.dp)
            .height(350.dp)
            .padding(8.dp)
            .clickable {
                viewModel.selectedStockItem = "no_idea_what_to_put_here_yet"
                viewModel.stockItemName = stockItem.stockItemName
                viewModel.stockItemQuantity = stockItem.stockItemQuantity
                viewModel.stockItemImageUri = stockItem.stockItemImagePath
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val imagePath = stockItem.stockItemImagePath
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/images/stock/${stockItem.stockItemImagePath}")
                        .build()
                ),
                contentDescription = stockItem.stockItemName,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stockItem.stockItemName,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* HANDLE AVAILABILITY TOGGLE */ },
                modifier = Modifier.width(150.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (isAvailable) Color.Green else Color.Gray
//                )
            ) {
                Text(
                    text = "Available",
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* HANDLE AVAILABILITY TOGGLE */ },
                modifier = Modifier.width(150.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (isAvailable) Color.Gray else Color.Red
//                )
            ) {
                Text(
                    text = "Out of Stock",
                    color = Color.White
                )
            }
        }
    }
}

// Composable for the add stock item card
@Composable
fun AddNewStockItemCard() {
    Card(
        modifier = Modifier
            .width(270.dp)
            .height(350.dp)
            .padding(8.dp)
            .clickable {
//                onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Add New Item",
                color = Color.Gray
            )
        }
    }
}

// Composable for the display of current action on the selected stock item card
@Composable
fun StockItemActionDisplay(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier,
) {
    if (viewModel.selectedStockItem != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (viewModel.selectedStockItem == "New Item") stringResource(R.string.add_new_item) else stringResource(R.string.edit_item),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.size(256.dp)) {
                viewModel.stockItemImageUri?.let { it ->
                    val currentStockItemImageUri = "file:///android_asset/images/stock/$it"
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentStockItemImageUri)
                                .build()
                        ),
                        contentDescription = viewModel.stockItemName,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                Icon(
                    painter = painterResource(R.drawable.edit), // Use your own drawable resource
                    contentDescription = "Change Image",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(4.dp)
                        .clickable { /* Handle image change action */ }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            BasicTextField(
                value = viewModel.stockItemName,
                onValueChange = { viewModel.stockItemName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        RoundedCornerShape(8.dp)
                    ) // Change background color to white
                    .padding(8.dp),
                singleLine = true,
                enabled = viewModel.selectedStockItem == "New Item"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(0.35f)) {
                    Text(
                        text = "Stock:",
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier.weight(0.6f),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(onClick = { (viewModel.stockItemQuantity)++ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                            Text(text = "+")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier.width(64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = "${viewModel.stockItemQuantity}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = { (viewModel.stockItemQuantity)-- }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))) {
                            Text(text = "-")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = { /* Save Action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFF6200EE), RoundedCornerShape(8.dp))
                ) {
                    Text(text = "Save", color = Color.White)
                }
                Button(
                    onClick = {
                        viewModel.selectedStockItem = null
                        viewModel.stockItemName = ""
                        viewModel.stockItemQuantity = 0
                        viewModel.stockItemImageUri = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    Text(text = "Cancel", color = Color.White)
                }
                Button(
                    onClick = { /* Delete Action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color.Red, RoundedCornerShape(8.dp))
                ) {
                    Text(text = "Delete", color = Color.White)
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No item chosen", color = Color.Gray, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
//    if (viewModel.selectedStockItem != null) {
//        Column(
//            modifier = modifier
//                .background(backgroundColor)
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//            Box(modifier = Modifier.size(100.dp)) {
//                viewModel.stockItemImageUri?.let { it ->
//                    Image(
//                        painter = rememberAsyncImagePainter(it),
//                        contentDescription = viewModel.selectedStockItem,
//                        modifier = Modifier.fillMaxSize()
//                    ) ?: Image(
//                        painter = painterResource(R.drawable.ic_launcher_foreground),
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                    Icon(
//                        painter = painterResource(R.drawable.edit),
//                        contentDescription = "Change Image",
//                        tint = Color.White,
//                        modifier = Modifier
//                            .align(Alignment.BottomEnd)
//                            .size(24.dp)
//                            .clip(CircleShape)
//                            .background(Color.Black.copy(alpha = 0.6f))
//                            .padding(4.dp)
//                            .clickable {
//                                /* image change action */
//                            }
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    BasicTextField(
//                        value = viewModel.stockItemName,
//                        onValueChange = { viewModel.stockItemName = it },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(
//                                Color.White,
//                                RoundedCornerShape(8.dp)
//                            )
//                            .padding(8.dp),
//                        singleLine = true,
//                        enabled = viewModel.selectedStockItem == "New Item" /* what is this? */
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(
//                            text = "Stock: ",
//                            fontWeight = FontWeight.Bold
//                        )
//                        Row {
//                            Button(
//                                /*
//                                    Note:
//                                        We can decrease the stockItemCount mutableStateOfValue,
//                                        then use that value to push an update to the database.
//                                 */
//                                onClick = { (viewModel.stockItemQuantity)++ },
//                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
//                            ) {
//                                Text(text = "+")
//                            }
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "${viewModel.stockItemQuantity}",
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 20.sp
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Button(
//                                onClick = { (viewModel.stockItemQuantity)-- },
//                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
//                            ) {
//                                Text(text = "-")
//                            }
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(100.dp))
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(8.dp),
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Button(
//                            onClick = { /* Save action, i.e. Dao invocation. */ },
//                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(48.dp)
//                                .background(Color(0xFF6200EE), RoundedCornerShape(8.dp))
//                        ) {
//                            Text(text = "Save", color = Color.White)
//                        }
//                        Button(
//                            onClick = {
//                                viewModel.selectedStockItem = null
//                                viewModel.stockItemName = ""
//                                viewModel.stockItemQuantity = 0
//                                viewModel.stockItemImageUri = null
//                            },
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(48.dp)
//                                .background(Color.Gray, RoundedCornerShape(8.dp))
//                        ) {
//                            Text(text = "Cancel", color = Color.White)
//                        }
//                        Button(
//                            onClick = { /* Delete action, i.e. Dao invocation. But this is complicated. */ },
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(48.dp)
//                                .background(Color.Red, RoundedCornerShape(8.dp))
//                        ) {
//                            Text(text = "Delete", color = Color.White)
//                        }
//                    }
//                }
//            }
//        }
//    } else {
//        Log.d("SelectedStockItem", "Currently null.")
//        Box(
//            modifier = modifier
//                .background(backgroundColor)
//                .fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = "No item chosen",
//                color = Color.Gray,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
}