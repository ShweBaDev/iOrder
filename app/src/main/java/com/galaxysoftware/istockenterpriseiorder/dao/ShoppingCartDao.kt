package com.galaxysoftware.istockenterpriseiorder.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.galaxysoftware.istockenterpriseiorder.models.ShoppingCartItem
import com.galaxysoftware.istockenterpriseiorder.models.User

@Dao
interface ShoppingCartDao {
    @Insert
    suspend fun Insert(cartItem : ShoppingCartItem)

    @Delete
    suspend fun Delete(cartItem: ShoppingCartItem)

    @Query("SELECT * FROM tbl_shoppingcartitem")
    fun Get(): LiveData<List<ShoppingCartItem>>

    @Query("SELECT COUNT(*) FROM tbl_shoppingcartitem")
    fun GetItemCount() : LiveData<Int>

    @Query("SELECT * FROM tbl_shoppingcartitem WHERE sr = :Id")
    suspend fun GetById(Id : Long) : ShoppingCartItem?

    @Query("DELETE FROM tbl_shoppingcartitem WHERE sr = :Id")
    suspend fun DeleteById(Id : Long)

    @Query("DELETE FROM tbl_shoppingcartitem")
    suspend fun ClearAll()
}