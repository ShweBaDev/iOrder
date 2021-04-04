package com.galaxysoftware.istockenterpriseiorder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.galaxysoftware.istockenterpriseiorder.dao.ShoppingCartDao
import com.galaxysoftware.istockenterpriseiorder.dao.UserDao
import com.galaxysoftware.istockenterpriseiorder.dao.VoucherIdDao
import com.galaxysoftware.istockenterpriseiorder.models.ShoppingCartItem
import com.galaxysoftware.istockenterpriseiorder.models.User
import com.galaxysoftware.istockenterpriseiorder.models.VoucherId

@Database(entities = [User::class,ShoppingCartItem::class,VoucherId::class], version = 2, exportSchema = false)
abstract class OrderDatabase : RoomDatabase(){

    abstract  val userDao : UserDao

    abstract  val shoppingCartDao : ShoppingCartDao

    abstract val voucherIdDao : VoucherIdDao

    companion object    {
        @Volatile
        private var INSTANCE : OrderDatabase? = null

        fun getInstance(context: Context) : OrderDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        OrderDatabase::class.java,
                        "order_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return  instance
            }
        }
    }
}