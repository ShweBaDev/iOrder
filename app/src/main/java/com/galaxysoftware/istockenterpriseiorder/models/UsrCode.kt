package com.galaxysoftware.istockenterpriseiorder.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_usrcode")
data class UsrCode (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name= "usrcode")
    val usrcode : String,

    @ColumnInfo(name ="description")
    var description : String?,

    @ColumnInfo(name= "shortdesc")
    val shortDesc : String?,

    @ColumnInfo(name="saleprice")
    val saleprice : Double?,

    @ColumnInfo(name = "imageurl")
    val imageurl : String?,

    @ColumnInfo(name="units")
    val units : List<UnitCode>
)