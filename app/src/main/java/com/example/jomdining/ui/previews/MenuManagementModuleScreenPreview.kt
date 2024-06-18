package com.example.jomdining.ui.previews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.jomdining.R
import com.example.jomdining.data.TempMenuItems
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.ui.JomDiningTopAppBar
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.sp
import okhttp3.internal.platform.Jdk9Platform.Companion.isAvailable

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "pixel_tablet_landscape",
    device = "spec: width=2560dp, height=1600dp, orientation=landscape",
    showBackground = true,
    backgroundColor = 0xCEDFFF
)
@Composable
fun MenuManagementModulePreview(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            JomDiningTopAppBar(
                title = "JomDining"
            )
        }
    ) { innerPadding ->
        Row(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(16.dp)
                    .fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                TestMenuItemGrid_II(modifier = modifier)
            }
            TestEditMenuActionDisplay(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .background(Color(0xFFD9E6FF))
                    .padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun MenuCardPreview() {
    val menuItem =
        Menu(
            menuItemID = 1,
            menuItemName = stringResource(R.string.chicken_chop),
            menuItemPrice = 12.34,
            menuItemType = stringResource(R.string.main_course),
            menuItemImagePath = "file:///android_asset/chickenChop.png",
            menuItemAvailability = 1
        )
    TestMenuCard(menuItem = menuItem)
}

@Composable
fun TestMenuItemGrid_II(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFCEDFFF)
) {
    LazyColumn(
        modifier = modifier
            .background(backgroundColor)
    ) {
        items(TempMenuItems.menuItems.size) { index ->
            TestMenuCard(TempMenuItems.menuItems[index])
            Spacer(modifier = Modifier.height(16.dp)) // Add spacing between items
        }
        item {
            TestAddNewMenuItemCard(modifier = modifier)
        }
    }
}

@Composable
fun TestMenuCard(
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
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group A: Menu image
            Column(modifier = modifier.weight(0.3f)) {
                Image(
                    painter= rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(menuItem.menuItemImagePath)
                            .build()
                    ),
                    contentDescription = menuItem.menuItemName,
                    modifier = Modifier
                        .size(192.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            // Group B: Menu name and price
            Column(
                modifier = modifier.padding(16.dp).weight(0.3f)
            ) {
                // Menu name in the center with its price
                Text(
                    text = menuItem.menuItemName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = String.format("RM %.2f", menuItem.menuItemPrice),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            // Group C: Available and out of stock buttons
            Column(modifier = modifier.weight(0.4f)) {
                Button(
                    onClick = { /* isAvailable = true */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green, /* if (isAvailable) Color.Green else Color.Gray */
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = "Available")
                }
                Button(
                    onClick = { /* isAvailable = false */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red, /* if (isAvailable) Color.Red else Color.Gray */
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Out of Stock",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun TestAddNewMenuItemCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .aspectRatio(4.5f)
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .clickable { /* Handle add new menu item */ },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Add New Item", style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestEditMenuActionDisplay(
    modifier: Modifier = Modifier
) {
    var ingredientItems by remember { mutableStateOf(listOf("Chicken", "Egg", "Vegetables")) }
    var checkedStates by remember { mutableStateOf(List(ingredientItems.size) { false }) }
    var newIngredientName by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .background(Color(0xFFE6E6E6))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center the contents horizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = "file:///android_asset/images/chickenChop.png"
            ),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = "",
            onValueChange = { /* Handle text change */ },
            label = { Text("Dish name") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = "",
            onValueChange = { /* Handle text change */ },
            label = { Text("Dish price") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // List of Ingredients with Checkboxes
        ingredientItems.forEachIndexed { index, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = checkedStates[index],
                    onCheckedChange = { isChecked ->
                        checkedStates = checkedStates.toMutableList().also {
                            it[index] = isChecked
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        // Add Ingredient
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            TextField(
                value = newIngredientName,
                onValueChange = { newIngredientName = it },
                label = { Text("New Ingredient") },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newIngredientName.isNotBlank()) {
                        ingredientItems = ingredientItems.toMutableList().apply {
                            add(newIngredientName)
                        }
                        checkedStates = checkedStates.toMutableList().apply {
                            add(false)
                        }
                        newIngredientName = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A148C))
            ) {
                Text(text = "Add", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Handle save */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A148C)),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Save", color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* Handle cancel */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C)),
                modifier = Modifier.weight(1f)
            ) {
                //this bbutton should be lighter shade, orwhiteish
                Text(text = "Cancel", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* Handle delete */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Delete", color = Color.White)
        }
    }
}