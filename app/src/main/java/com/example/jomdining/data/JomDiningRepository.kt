package com.example.jomdining.data

import com.example.jomdining.databaseentities.Account
import com.example.jomdining.databaseentities.Menu
import com.example.jomdining.databaseentities.OrderItem
import com.example.jomdining.databaseentities.Stock
import com.example.jomdining.databaseentities.Transactions
import kotlinx.coroutines.flow.Flow

interface JomDiningRepository {
    /*
        ALL ITEMS UNDER AccountDao
     */
    suspend fun getAccountByLoginDetailsStream(loginUsername: String, loginPassword: String): Account

    suspend fun createNewAccountStream(accountUsername: String, accountPassword: String, accountEmail: String): Long

    /*
        ALL ITEMS UNDER MenuDao
     */
    fun getAllMenuItems(): Flow<List<Menu>>

    /*
        ALL ITEMS UNDER OrderItemDao
     */
    suspend fun addNewOrderItemStream(transactionID: Int, menuItemID: Int)

    suspend fun deleteOrderItemStream(transactionID: Int, menuItemID: Int)

    suspend fun getOrderItemByID(transactionID: Int, menuItemID: Int): OrderItem

    suspend fun getCorrespondingMenuItemStream(menuItemID: Int): Menu

    fun getAllOrderItemsByTransactionIDStream(transactionID: Int): Flow<List<OrderItem>>

    suspend fun increaseOrderItemQuantityStream(transactionID: Int, menuItemID: Int)

    suspend fun decreaseOrderItemQuantityStream(transactionID: Int, menuItemID: Int)

    /*
        ALL ITEMS UNDER TransactionsDao
     */
    suspend fun createNewTransactionUnderAccountStream(newAccountID: Long)
    suspend fun getCurrentActiveTransactionStream(accountID: Int): Transactions

    suspend fun updateRunningTotalStream(runningTotal: Long, accountID: Int)

    /*
        ALL ITEMS UNDER StockDao
     */
    suspend fun addNewStockItemStream(stockItemName: String, stockItemQuantity: Int)

    suspend fun updateStockItemDetailsStream(stockItemID: Int, newStockItemName: String, newStockItemQuantity: Int)

    suspend fun deleteStockItemStream(stockItemID: Int)

    fun getAllStockItems(): Flow<List<Stock>>
}