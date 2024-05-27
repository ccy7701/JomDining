package com.example.jomdining.data

import kotlinx.coroutines.flow.Flow

class OfflineRepository(
    private val accountDao: AccountDao,
    private val menuDao: MenuDao,
    private val menuItemIngredientDao: MenuItemIngredientDao,
    private val orderItemDao: OrderItemDao,
    private val stockDao: StockDao,
    private val transactionDao: TransactionDao
) : JomDiningRepository {

}
