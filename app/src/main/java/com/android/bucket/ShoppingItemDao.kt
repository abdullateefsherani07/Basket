package com.android.bucket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ShoppingItemDao {
    @Insert
    suspend fun insert(shoppingItemEntity: ShoppingItemEntity)

    @Update
    suspend fun update(shoppingItemEntity: ShoppingItemEntity)

    @Delete
    suspend fun delete(shoppingItemEntity: ShoppingItemEntity)

    @Query("SELECT * FROM `shopping-items-tabel`")
    fun fetchAllShoppingItems(): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM `shopping-items-tabel` where id = :id")
    fun fetchShoppingItemById(id: Int): Flow<ShoppingItemEntity>
}