package com.galaxysoftware.istockenterpriseiorder.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_category")
data class Category (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name= "categoryid")
    val categoryid : Int,

    @ColumnInfo(name ="name")
    var name : String

)