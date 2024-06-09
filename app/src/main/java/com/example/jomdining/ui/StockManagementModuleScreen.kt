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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
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
            StockItemCard(stockItem)
        }
        item {
            AddNewStockItemCard()
        }
    }
}

// Composable for an individual stock item card
@Composable
fun StockItemCard(
    stockItem: Stock,
) {
    Card(
        modifier = Modifier
            .width(270.dp)
            .height(350.dp)
            .padding(8.dp)
            .clickable {
                // onClick() operation here
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
    backgroundColor: Color = Color(0xBFBFBFFF)
) {
    if (viewModel.selectedStockItem != null) {
        Log.d("SelectedStockItem", "Currently not null. Value: ${viewModel.selectedStockItem}")
    } else {
        Log.d("SelectedStockItem", "Currently null.")
    }
}