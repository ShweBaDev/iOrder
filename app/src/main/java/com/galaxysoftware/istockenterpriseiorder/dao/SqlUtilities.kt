package com.galaxysoftware.istockenterpriseiorder.dao

import android.content.Context
import androidx.lifecycle.LiveData
import com.galaxysoftware.istockenterpriseiorder.database.OrderDatabase
import com.galaxysoftware.istockenterpriseiorder.models.ShoppingCartItem
import com.galaxysoftware.istockenterpriseiorder.models.User
import com.galaxysoftware.istockenterpriseiorder.models.VoucherId
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SqlUtilities  {

    private  var db : OrderDatabase?= null

    constructor (context: Context){
        db = OrderDatabase.getInstance(context)
    }

    fun saveUsers(users :List<User>) {
            GlobalScope.async {
                db?.userDao?.InsertUsers(users)
            }
        }


    fun getUsersDemo(callback: (List<User>?) -> Unit){
        var users: List<User>? = listOf<User>()
       GlobalScope.launch {
           users = db?.userDao?.Get()
        }.invokeOnCompletion{
            cause ->
            if(cause == null){
                callback(users)
            }
            else
            {
                cause
            }
        }
    }

    fun getUsers(): MutableList<User>?{
        var users: MutableList<User>? = mutableListOf<User>()
        GlobalScope.async {
          db?.userDao?.Get()?.forEach {
              users?.add(it)
          }
          }

        return users
    }

    fun saveCartItem(cartItem: ShoppingCartItem){
        GlobalScope.async {
            db?.shoppingCartDao?.Insert(cartItem)
        }
    }

    fun deleteCartItem(cartItem: ShoppingCartItem){
        GlobalScope.async {
            db?.shoppingCartDao?.Delete(cartItem)
        }
    }

    fun deleteCartItems(){
        GlobalScope.async {
            db?.shoppingCartDao?.ClearAll()
        }
    }

    fun getCartItemCount() : LiveData<Int>?{
        return db?.shoppingCartDao?.GetItemCount()
    }

    fun getCartItems() : LiveData<List<ShoppingCartItem>>?{
        return db?.shoppingCartDao?.Get()
    }

    fun getDocId() : LiveData<List<VoucherId>>?{
        return  db?.voucherIdDao?.Get()
    }

    fun saveDocId(voucherId: VoucherId){
        GlobalScope.async {
            db?.voucherIdDao?.Insert(voucherId)
        }
    }

    fun deleteDocId(){
        GlobalScope.async {
            db?.voucherIdDao?.DeleteAll()
        }
    }

}