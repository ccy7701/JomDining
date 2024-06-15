package com.example.jomdining.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun OrderHistoryModuleScreen(
    viewModel: JomDiningViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // Fetch current order history list when this screen is composed
    val activeLoginAccount by viewModel.activeLoginAccount.observeAsState()
    LaunchedEffect(activeLoginAccount) {
        activeLoginAccount?.let { account ->
            account.accountID.let { accountID ->
                // viewModel.getAllHistoricalTransactions(accountID)
            }
        }
    }
}