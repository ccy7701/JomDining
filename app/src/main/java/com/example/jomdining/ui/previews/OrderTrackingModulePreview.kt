package com.example.jomdining.ui.previews

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jomdining.data.TestOrderTrackingItems
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Transactions

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "pixel_tablet_landscape_alternate",
    device = "spec: width=2560dp, height=1600dp, orientation=landscape",
    showBackground = true,
    backgroundColor = 0xFFCEDFFF
)
@Composable
fun OrderTrackingModulePreview(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            JomDiningTopAppBarPreview(title = "JomDining")
        },
        containerColor = Color(0xCEDFFFFF)
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
                    TestOrderTrackingGrid()
                }
            }
        }
    }
}

@Composable
fun TestOrderTrackingGrid(
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth()
    ) {
        val testListToUse = TestOrderTrackingItems.completeTrackingList
        items(testListToUse) {  completeItem ->
            TestTransactionCard(completeItem)
        }
    }
}

@Composable
fun TestTransactionCard(
    completeTransaction: Pair<Transactions, List<Pair<OrderItem, Menu>>>
) {
    val transaction = completeTransaction.first
    // val orderItemsList = completeTransaction.second

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
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.width(100.dp)) { Text(text = "Transaction ID ") }
            Spacer(modifier = Modifier.width(16.dp))
            Column() { Text(text = "${transaction.transactionID}", fontSize = 30.sp, fontWeight = FontWeight.Bold) }
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)) {
            Column(modifier = Modifier.width(100.dp)) { Text(text = "Date ") }
            Spacer(modifier = Modifier.width(16.dp))
            Column() { Text(text = transaction.transactionDateTime, fontSize = 20.sp) }
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)) {
            Column(modifier = Modifier.width(100.dp)) { Text(text = "Sent by") }
            Spacer(modifier = Modifier.width(16.dp))
            Column() { Text(text = "${transaction.accountID}", fontSize = 20.sp) }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // OrderItemListDisplay(transaction.transactionID, orderItemsList)
    }
}