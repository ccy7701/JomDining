package com.example.jomdining.ui.previews

import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.jomdining.R
import com.example.jomdining.data.TempMenuItems
import com.example.jomdining.data.TestOrderItemsWithMenus
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.ui.JomDiningTopAppBar
import com.example.jomdining.ui.JomDiningViewModel
import com.example.jomdining.ui.MenuItemCard
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "pixel_tablet_landscape",
    device = "spec: width=2560dp, height=1600dp, orientation=landscape",
    showBackground = true,
    backgroundColor = 0xCEDFFF
)
@Composable
fun FoodOrderingModulePreview(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            JomDiningTopAppBar(
                title = "JomDining"
            )
        }
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
                    TestMenuItemGrid(modifier = modifier)
                }
                TestOrderSummary(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight()
                        .background(Color(0xFFD9E6FF))
                )
            }
        }
    }
}

@Preview
@Composable
fun MenuItemCardPreview() {
    val menuItem =
        Menu(
            menuItemID = 1,
            menuItemName = stringResource(R.string.chicken_chop),
            menuItemPrice = 12.34,
            menuItemType = stringResource(R.string.main_course),
            menuItemImagePath = "file:///android_asset/chickenChop.png"
        )
    TestMenuItemCard(menuItem = menuItem,)
}

@Preview
@Composable
fun OrderItemCardPreview() {
    TestOrderItemCard()
}

@Composable
fun TestMenuItemGrid(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFCEDFFF)
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .background(backgroundColor)
    ) {
        items(TempMenuItems.menuItems) { menuItem ->
            TestMenuItemCard(menuItem)
        }
    }
}

@Composable
fun TestMenuItemCard(
    menuItem: Menu,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(16.dp)
            .clickable {},
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
                            .data("file:///android_asset/$imagePath")
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
fun TestOrderSummary(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFFE6E6E6))
            .padding(16.dp)
    ) {
        Text(
            text = "Order #12345",
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
            items(TestOrderItemsWithMenus.orderItemsWithMenus) { pair ->
                TestOrderItemCard()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "RM 65.00",
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
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .width(60.dp)
                    .height(40.dp)
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

@Composable
fun TestOrderItemCard(modifier: Modifier = Modifier) {
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
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/images/chickenChop.png")
                        .build()
                ),
                contentDescription = "Ordered Item",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text("Fried Chicken", fontWeight = FontWeight.Bold)
                Text("RM 20.00", color = Color(0xFF7C4DFF))
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
                        .clickable { /* Decrease Quantity */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Reduce Quantity",
                        tint = Color.White
                    )
                }
                Box(
                    modifier = Modifier.width(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1",
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Green)
                        .clickable { /* Increase Quantity */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Quantity",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { /* Delete Item */ }) {
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