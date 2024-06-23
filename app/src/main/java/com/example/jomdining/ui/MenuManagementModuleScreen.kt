package com.example.jomdining.ui

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.jomdining.ui.components.JomDiningTopAppBar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("AutoboxingStateCreation")
@Composable
fun MenuManagementModuleScreen(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    // Fetch Menu items when this screen is composed
    viewModel.getAllMenuItems()

    Scaffold(
        topBar = {
            JomDiningTopAppBar(
                title = "Menu Management",
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
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    // the left side composable
                    MenuCardGrid(
                        viewModel = viewModel,
                        modifier = modifier
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxSize()
                        .background(Color.LightGray)
                ) {
                    EditMenuActionDisplay(
                        viewModel = viewModel,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

// Composable for the grid of menu item cards
@Composable
fun MenuCardGrid(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFCEDFFF)
) {
    LazyColumn(
        modifier = modifier
            .background(backgroundColor)
    ) {
        items(viewModel.menuUi.menuItems) { menu ->
            MenuCard(viewModel, menu)
        }
        item {
            AddMenuCard(viewModel)
        }
    }
}


@Composable
fun MenuCard(
    viewModel: JomDiningViewModel,
    menuItem: Menu,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val packageName = "com.example.jomdining"

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(16.dp)
            .clickable {
                viewModel.selectedMenuItem = "existing_item"
                viewModel.menuItemID = menuItem.menuItemID
                viewModel.menuItemName = menuItem.menuItemName
                viewModel.menuItemPrice =
                    String.format(Locale.getDefault(), "%.2f", menuItem.menuItemPrice)
                viewModel.menuItemType = menuItem.menuItemType
                viewModel.menuItemImageUri = menuItem.menuItemImagePath
                viewModel.menuItemAvailability = menuItem.menuItemAvailability
                focusManager.clearFocus()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (menuItem.menuItemAvailability == -1) Color(0xFFA95C68) else White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group A: Menu image
            Column(modifier = modifier.weight(0.3f)) {
                val imagePath = menuItem.menuItemImagePath
                val resourceID = context.resources.getIdentifier(imagePath, "drawable", packageName)
                Image(
                    painter= rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(
                                if (imagePath != "") { resourceID }
                                else { R.drawable.jomdininglogo }
                            )
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
                modifier = modifier
                    .padding(16.dp)
                    .weight(0.3f)
            ) {
                // Menu name in the center with its price
                Text(
                    text = menuItem.menuItemName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (menuItem.menuItemAvailability == -1) White else Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = String.format(Locale.getDefault(), "RM %.2f", menuItem.menuItemPrice),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (menuItem.menuItemAvailability == -1) White else Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            // Group C: Available and out of stock buttons
            Column(
                modifier = modifier.weight(0.4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (menuItem.menuItemAvailability == -1) {
                    Text(
                        text = "MENU ITEM RETIRED",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    Button(
                        onClick = { viewModel.updateMenuAvailability(menuItem.menuItemID, 1) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (menuItem.menuItemAvailability == 1) Color(0xFF415F91) else Color.LightGray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = "Available")
                    }
                    Button(
                        onClick = { viewModel.updateMenuAvailability(menuItem.menuItemID, 0) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (menuItem.menuItemAvailability == 0) Color(0xFF415F91) else Color.LightGray
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
}

@Composable
fun AddMenuCard(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(16.dp)
            .clickable {
                viewModel.selectedMenuItem = "new_item"
                viewModel.menuItemID = -1
                viewModel.menuItemName = "New menu item"
                viewModel.menuItemPrice = ""
                viewModel.menuItemType = ""
                viewModel.menuItemImageUri = ""
                viewModel.menuItemAvailability = 1
                focusManager.clearFocus()
                Log.d(
                    "MC_menuItem",
                    "Currently adding new menu item, menuItemID set to ${viewModel.menuItemID}"
                )
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(192.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Add New Menu Item",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 24.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuActionDisplay(
    viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val packageName = "com.example.jomdining"

    if (viewModel.selectedMenuItem != null) {
        if (viewModel.menuItemAvailability == -1) {
            Column(
                modifier = modifier
                    .background(Color(0xFFA95C68))
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val imagePath = viewModel.menuItemImageUri
                val resourceID = context.resources.getIdentifier(imagePath, "drawable", packageName)
                Image(
                    painter= rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(
                                if (imagePath != "") { resourceID }
                                else { R.drawable.jomdininglogo }
                            )
                            .build()
                    ),
                    contentDescription = viewModel.menuItemName,
                    modifier = Modifier
                        .size(192.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewModel.menuItemName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "This menu item was retired.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = White,
                    fontSize = 24.sp
                )
            }
        } else {
            Column(
                modifier = modifier
                    .background(Color(0xFFE6E6E6))
                    .fillMaxHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val imagePath = viewModel.menuItemImageUri
                val resourceID = context.resources.getIdentifier(imagePath, "drawable", packageName)
                Image(
                    painter= rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(
                                if (imagePath != "") { resourceID }
                                else { R.drawable.jomdininglogo }
                            )
                            .build()
                    ),
                    contentDescription = viewModel.menuItemName,
                    modifier = Modifier
                        .size(192.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = viewModel.menuItemName,
                    onValueChange = { viewModel.menuItemName = it },
                    label = { Text(stringResource(R.string.menu_item_name)) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = viewModel.menuItemPrice,
                    onValueChange = {
                        viewModel.menuItemPrice = it
                    },
                    label = { Text("Menu item price") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = { Text("RM ") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var menuItemTypeDropdownExpanded by remember { mutableStateOf(false) }
                    val allMenuItemTypes = listOf("main_course", "side_dish", "beverage")
                    Text(
                        text = stringResource(R.string.menu_item_type),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Box(
                        modifier = Modifier
                            .width(144.dp)
                            .height(48.dp)
                            .background(White, shape = RoundedCornerShape(8.dp))
                            .clickable { menuItemTypeDropdownExpanded = true },
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(text = viewModel.menuItemType, modifier = Modifier.padding(end = 8.dp))
                        DropdownMenu(
                            expanded = menuItemTypeDropdownExpanded,
                            onDismissRequest = { menuItemTypeDropdownExpanded = false },
                            modifier = Modifier
                                .width(144.dp)
                                .padding(top = 4.dp)
                        ) {
                            allMenuItemTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        viewModel.menuItemType = type
                                        menuItemTypeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            try {
                                // First, parse the textField string to double
                                val pushMenuItemPrice = viewModel.menuItemPrice.toDoubleOrNull()
                                val pushMenuItemType = viewModel.menuItemType

                                // Then, the data has to go through all these checks and pass them all before pushing to the DB
                                if (pushMenuItemType == "") {
                                    Toast
                                        .makeText(
                                            context,
                                            "Please make sure to select a menu item type.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    return@Button
                                }
                                if (pushMenuItemPrice == null) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Please check your menu item price input again.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    return@Button
                                }

                                // Then, call the appropriate viewModel function
                                if (viewModel.selectedMenuItem == "existing_item") {
                                    viewModel.updateMenuItemDetails(
                                        menuItemID = viewModel.menuItemID,
                                        menuItemName = viewModel.menuItemName,
                                        menuItemPrice = pushMenuItemPrice,
                                        menuItemType = pushMenuItemType
                                    )
                                    Toast
                                        .makeText(
                                            context,
                                            "Menu item edited successfully.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                } else if (viewModel.selectedMenuItem == "new_item") {
                                    viewModel.addNewMenuItem(
                                        menuItemName = viewModel.menuItemName,
                                        menuItemPrice = pushMenuItemPrice,
                                        menuItemType = pushMenuItemType
                                    )
                                    Toast
                                        .makeText(
                                            context,
                                            "New menu item added successfully.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }

                                // Then, clear the data at the viewModel.
                                viewModel.selectedMenuItem = null
                                viewModel.menuItemID = -1
                                viewModel.menuItemName = ""
                                viewModel.menuItemPrice = ""
                                viewModel.menuItemImageUri = ""
                            } catch (e: Exception) {
                                Log.e("addOrEditMenuItem", "Error encountered: $e")
                                Toast
                                    .makeText(
                                        context,
                                        "An error was encountered. Please try again.",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A148C)),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (viewModel.selectedMenuItem == "existing_item") { Text(text = stringResource(R.string.save), color = White) }
                        else if (viewModel.selectedMenuItem == "new_item") { Text(text = stringResource(R.string.insert_new_item), color = White) }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            viewModel.selectedMenuItem = null
                            viewModel.menuItemID = -1
                            viewModel.menuItemName = ""
                            viewModel.menuItemPrice = ""
                            viewModel.menuItemImageUri = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C)),
                        modifier = Modifier.weight(1f)
                    ) {
                        // this button should be lighter shade, or white-ish
                        Text(text = "Cancel", color = White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (viewModel.selectedMenuItem == "existing_item") {
                    var showRetireConfirmationDialog by remember { mutableStateOf(false) }
                    Button(
                        onClick = { showRetireConfirmationDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.retire_this_menu_item), color = White)
                    }
                    // Confirmation dialog
                    if (showRetireConfirmationDialog) {
                        AlertDialog(
                            onDismissRequest = { showRetireConfirmationDialog = false},
                            title = { Text(text = "Confirm retirement") },
                            text = { Text(text = "Are you sure you want to retire ${viewModel.menuItemName}? This action cannot be undone!") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        // Invoke the retirement function from the viewModel
                                        viewModel.retireMenuItem(viewModel.menuItemID)
                                        // Close down the MenuItemActionDisplay, revert it to starting view
                                        viewModel.selectedMenuItem = null
                                        viewModel.menuItemID = -1
                                        viewModel.menuItemName = ""
                                        viewModel.menuItemPrice = ""
                                        viewModel.menuItemImageUri = ""
                                        // Display a Toast message indicating the process has passed
                                        Toast
                                            .makeText(
                                                context,
                                                "Menu item retired successfully.",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                        // Hide the dialog
                                        showRetireConfirmationDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Red)
                                ) {
                                    Text(text = "Confirm", color = White)
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { showRetireConfirmationDialog = false }
                                ) {
                                    Text(text = "Cancel")
                                }
                            },
                            properties = DialogProperties(dismissOnClickOutside = true)
                        )
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