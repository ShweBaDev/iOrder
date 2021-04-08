package com.galaxysoftware.istockenterpriseiorder.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tbl_customeroutstand")
data class CustOutstand (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name= "name")
    val name : String,

    @ColumnInfo(name= "date")
    val date : Date,

    @ColumnInfo(name= "invoicetype")
    val invoicetype : String?,

    @ColumnInfo(name ="docid")
    var docid : String?,

    @ColumnInfo(name ="amount")
    var amount : Double?

)
