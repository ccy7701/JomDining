package com.example.jomdining.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jomdining.ui.previews.TestStockManagementScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockManagementModuleScreen(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier
) {
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
            StockManagementScreen(viewModel, modifier)
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun StockManagementScreen(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier
) {
    var selectedIngredient by remember { mutableStateOf<String?>(null) }
    var ingredientName by remember { mutableStateOf("") }
    var stockCount by remember { mutableStateOf(24) }
    var ingredientImageUri by remember { mutableStateOf<String?>(null) }

    // Left portion
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCEDFFF))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(end = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StockItemCard(
                    "Egg",
                    true,
                    "file:///android_asset/images/stock/egg.png"
                ) {
                    selectedIngredient = "Ingredient 1"
                    ingredientName = "Egg"
                    ingredientImageUri = "file://android_asset/images/stock/egg.png"
                }
            }
        }
    }
}

// Composable for an individual stock item card
@Composable
fun StockItemCard(
    name: String,
    isAvailable: Boolean,
    imageUri: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(270.dp)
            .height(300.dp)
            .padding(8.dp)
            .clickable {
                       onClick()
            },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}