package com.example.jomdining.ui.previews

import android.graphics.Paint.Align
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.jomdining.R
import com.example.jomdining.ui.JomDiningTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "pixel_tablet_landscape_alternate",
    device = "spec: width=2560dp, height=1600dp, orientation=landscape",
    showBackground = true,
    backgroundColor = 0xCEDFFF
)
@Composable
fun StockManagementModulePreview(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            JomDiningTopAppBar(title = "JomDining")
        },
        containerColor = Color(0xCEDFFFFF)
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
                    TestIngredientCardGrid()
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFCEDFFF))
                        .weight(0.4f)
                        .fillMaxSize()
                ) {
                    TestStockItemActionDisplay(
                        modifier = Modifier
                            .background(
                                Color.LightGray,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TestIngredientCardGrid(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFCEDFFF),
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 3),
        modifier = modifier.background(backgroundColor)
    ) {
        item { TestIngredientCard() }
        item { TestIngredientCard() }
        item { TestAddNewItemCard() }
    }
}

@Composable
fun TestIngredientCard() {
    var selectedIngredient by remember { mutableStateOf<String?>(null) }
    var ingredientName by remember { mutableStateOf("") }
    var stockCount by remember { mutableIntStateOf(0) }
    var ingredientImageUri by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier
            .width(270.dp)
            .height(350.dp)
            .padding(8.dp)
            .clickable {
                selectedIngredient = "TEST_SELECTED_INGREDIENT"
                ingredientName = "TEST_INGREDIENT_NAME"
                stockCount = 1234
                ingredientImageUri = "TEST_IMAGE_URI"
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // Center vertically
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/images/stock/egg.png")
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "TEST",
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

@Composable
fun TestAddNewItemCard(
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(270.dp)
            .height(350.dp) // Set the same height as other cards
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)) // Light blue card background
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Add New Item", color = Color.Gray)
        }
    }
}

@Composable
fun TestStockItemActionDisplay(
    modifier: Modifier,
    backgroundColor: Color = Color.LightGray
) {
    var selectedIngredient by remember { mutableStateOf<String?>(null) }
    var ingredientName by remember { mutableStateOf("") }
    var stockCount by remember { mutableIntStateOf(0) }
    var ingredientImageUri by remember { mutableStateOf<String?>(null) }

    if (selectedIngredient != null) {
        Column(
            modifier = modifier
                .background(backgroundColor)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (selectedIngredient == "New Item") "Add New Item" else "Edit Item",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(100.dp))
            Box(modifier = Modifier.size(100.dp)) {
                ingredientImageUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    ) ?: Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = "Change Image",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(4.dp)
                            .clickable {
                                /* image change action */
                            }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                BasicTextField(
                    value = ingredientName,
                    onValueChange = { ingredientName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    singleLine = true,
                    enabled = selectedIngredient == "New Item"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Stock: ", fontWeight = FontWeight.Bold)
                    Row {
                        Button(
                            onClick = { stockCount++ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text(text = "+")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "$stockCount", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { stockCount-- },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                        ) {
                            Text(text = "-")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { /* Save action */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(Color(0xFF6200EE), RoundedCornerShape(8.dp))
                    ) {
                        Text(text = "Save", color = Color.White)
                    }
                    Button(
                        onClick = { /* Cancel action */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(Color.Gray, RoundedCornerShape(8.dp))
                    ) {
                        Text(text = "Cancel", color = Color.White)
                    }
                    Button(
                        onClick = { /* Delete action */ },
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
        }
    } else {
        Box(
            modifier = modifier
                .background(backgroundColor)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No item chosen",
                color = Color.Gray,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}