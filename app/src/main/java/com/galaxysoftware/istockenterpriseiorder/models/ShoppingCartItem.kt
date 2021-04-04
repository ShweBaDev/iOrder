package com.galaxysoftware.istockenterpriseiorder.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_shoppingcartitem")
data class ShoppingCartItem (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "sr")
    val sr : Long? = null,

    @ColumnInfo(name= "voucherno")
    var voucherNo : String?,

    @ColumnInfo(name= "usrcode")
    var usrCode : String,

    @ColumnInfo(name = "description")
    var description : String? = null,

    @ColumnInfo(name = "unitqty")
    var unitqty : Double? = null,

    @ColumnInfo(name = "unittypeid")
    var unittypeid : Int? = 1,

    @ColumnInfo(name = "saleprice")
    var saleprice : Double? = null

)