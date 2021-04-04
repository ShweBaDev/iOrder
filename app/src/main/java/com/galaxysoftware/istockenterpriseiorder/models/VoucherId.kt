package com.galaxysoftware.istockenterpriseiorder.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tbl_voucherId")
data class VoucherId (
        @PrimaryKey(autoGenerate = false)
        var voucherno: String
)