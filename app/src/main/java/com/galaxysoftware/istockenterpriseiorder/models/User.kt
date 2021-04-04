package com.galaxysoftware.istockenterpriseiorder.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_user")
data class User (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name= "userid")
    val userid : Int,

    @ColumnInfo(name ="name")
    val name : String?,

    @ColumnInfo(name= "shortname")
    val shortName : String?,

    @ColumnInfo(name="password")
    val password : String?


)