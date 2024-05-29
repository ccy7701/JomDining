package com.example.jomdining.data

import com.example.jomdining.daos.AccountDao
import com.example.jomdining.daos.MenuDao
import com.example.jomdining.daos.MenuItemIngredientDao
import com.example.jomdining.daos.OrderItemDao
import com.example.jomdining.daos.StockDao
import com.example.jomdining.daos.TransactionsDao

class OfflineRepository(
    private val accountDao: AccountDao,
    private val menuDao: MenuDao,
    private val menuItemIngredientDao: MenuItemIngredientDao,
    private val orderItemDao: OrderItemDao,
    private val stockDao: StockDao,
    private val transactionsDao: TransactionsDao
) : JomDiningRepository {
    /*
        ALL ITEMS UNDER orderItemDao
     */
    override fun fetchOrderItemByID(transactionID: Int, menuItemID: Int) =
        orderItemDao.fetchOrderItemByID(transactionID, menuItemID)

    override suspend fun increaseOrderItemQuantity(transactionID: Int, menuItemID: Int) =
        orderItemDao.increaseOrderItemQuantity(transactionID, menuItemID)

    override suspend fun decreaseOrderItemQuantity(transactionID: Int, menuItemID: Int) {
        orderItemDao.decreaseOrderItemQuantity(transactionID, menuItemID)
    }
}
