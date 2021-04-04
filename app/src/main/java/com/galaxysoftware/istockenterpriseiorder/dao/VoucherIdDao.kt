package com.galaxysoftware.istockenterpriseiorder.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.galaxysoftware.istockenterpriseiorder.models.VoucherId

@Dao
interface VoucherIdDao {

    @Insert
    suspend fun Insert(voucher : VoucherId)

    @Update
    suspend fun Update(voucher: VoucherId)

    @Query("SELECT * FROM tbl_voucherId")
    fun Get(): LiveData<List<VoucherId>>

    @Query("DELETE FROM tbl_voucherId")
    suspend fun DeleteAll()
}