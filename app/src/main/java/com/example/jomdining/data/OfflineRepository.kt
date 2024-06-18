package com.example.jomdining.data

import com.example.jomdining.daos.AccountDao
import com.example.jomdining.daos.MenuDao
import com.example.jomdining.daos.OrderItemDao
import com.example.jomdining.daos.StockDao
import com.example.jomdining.daos.TransactionsDao
import com.example.jomdining.databaseentities.Menu

class OfflineRepository(
    private val accountDao: AccountDao,
    private val menuDao: MenuDao,
    private val orderItemDao: OrderItemDao,
    private val stockDao: StockDao,
    private val transactionsDao: TransactionsDao
) : JomDiningRepository {
    /*
        ALL ITEMS UNDER AccountDao
     */
    override suspend fun getAccountByLoginDetailsStream(loginUsername: String, loginPassword: String) =
        accountDao.getAccountByLoginDetails(loginUsername, loginPassword)

    override suspend fun createNewAccountStream(accountUsername: String, accountPassword: String, accountEmail: String): Long {
        return accountDao.createNewAccount(accountUsername, accountPassword, accountEmail)
    }

    /*
        ALL ITEMS UNDER MenuDao
     */
    override fun getAllMenuItems() =
        menuDao.getAllMenuItems()

    override suspend fun addNewMenuItemStream(menuItemName: String, menuItemPrice: Double, menuItemType: String) =
        menuDao.addNewMenuItem(menuItemName, menuItemPrice, menuItemType)

    override suspend fun updateMenuItemDetailsStream(menuItemID: Int, menuItemName: String, menuItemPrice: Double, menuItemType: String) =
        menuDao.updateMenuItemDetails(menuItemID, menuItemName, menuItemPrice, menuItemType)

    /*
        ALL ITEMS UNDER OrderItemDao
     */
    override suspend fun addNewOrderItemStream(transactionID: Int, menuItemID: Int) =
        orderItemDao.addNewOrderItem(transactionID, menuItemID)

    override suspend fun deleteOrderItemStream(transactionID: Int, menuItemID: Int) =
        orderItemDao.deleteOrderItem(transactionID, menuItemID)

    override suspend fun getOrderItemByID(transactionID: Int, menuItemID: Int) =
        orderItemDao.getOrderItemByID(menuItemID)

    override suspend fun getCorrespondingMenuItemStream(menuItemID: Int) =
        orderItemDao.getCorrespondingMenuItem(menuItemID)

    override fun getAllOrderItemsByTransactionIDStream(transactionID: Int) =
        orderItemDao.getAllOrderItemsByTransactionID(transactionID)

    override suspend fun increaseOrderItemQuantityStream(transactionID: Int, menuItemID: Int) =
        orderItemDao.increaseOrderItemQuantity(transactionID, menuItemID)

    override suspend fun decreaseOrderItemQuantityStream(transactionID: Int, menuItemID: Int) =
        orderItemDao.decreaseOrderItemQuantity(transactionID, menuItemID)

    /*
        ALL ITEMS UNDER TransactionsDao
     */
    override suspend fun createNewTransactionUnderAccountStream(newAccountID: Long) =
        transactionsDao.createNewTransactionUnderAccount(newAccountID)

    override suspend fun getCurrentActiveTransactionStream(accountID: Int) =
        transactionsDao.getCurrentActiveTransaction(accountID)

    override fun getAllHistoricalTransactionsStream(accountID: Int) =
        transactionsDao.getAllHistoricalTransactions(accountID)

    /*
        ALL ITEMS UNDER StockDao
     */
    override fun getAllStockItems() =
        stockDao.getAllStockItems()

    override suspend fun addNewStockItemStream(stockItemName: String, stockItemQuantity: Int) =
        stockDao.addNewStockItem(stockItemName, stockItemQuantity)

    override suspend fun updateStockItemDetailsStream(stockItemID: Int, newStockItemName: String, newStockItemQuantity: Int) =
        stockDao.updateStockItemDetails(stockItemID, newStockItemName, newStockItemQuantity)

    override suspend fun deleteStockItemStream(stockItemID: Int) =
        stockDao.deleteStockItem(stockItemID)
}
