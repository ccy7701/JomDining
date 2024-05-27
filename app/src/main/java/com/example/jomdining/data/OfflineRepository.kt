package com.example.jomdining.data

import com.example.jomdining.daos.AccountDao
import com.example.jomdining.daos.MenuDao
import com.example.jomdining.daos.MenuItemIngredientDao
import com.example.jomdining.daos.OrderItemDao
import com.example.jomdining.daos.StockDao
import com.example.jomdining.daos.TransactionDao

class OfflineRepository(
    private val accountDao: AccountDao,
    private val menuDao: MenuDao,
    private val menuItemIngredientDao: MenuItemIngredientDao,
    private val orderItemDao: OrderItemDao,
    private val stockDao: StockDao,
    private val transactionDao: TransactionDao
) : JomDiningRepository {

}
