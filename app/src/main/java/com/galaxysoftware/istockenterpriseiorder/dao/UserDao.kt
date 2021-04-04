package com.galaxysoftware.istockenterpriseiorder.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.galaxysoftware.istockenterpriseiorder.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun Insert(user : User)

    @Insert
    suspend fun InsertUsers(users : List<User>)

    @Update
    suspend fun Update(user: User)

    @Delete
    suspend fun Delete(user: User)

    @Query("SELECT * FROM tbl_user")
    fun Get(): List<User>

    @Query("SELECT * FROM tbl_user WHERE userid = :Id")
    suspend fun GetById(Id : Long) : User?

    @Query("DELETE FROM tbl_user")
    suspend fun ClearAll()
}