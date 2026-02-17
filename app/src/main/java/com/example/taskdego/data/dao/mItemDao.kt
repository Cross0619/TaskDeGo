package com.example.taskdego.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskdego.data.entity.mItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface mItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: mItemEntity)

    @Query("SELECT * FROM m_items WHERE m_item_id = :itemId")
    suspend fun getItemById(itemId: Int): mItemEntity?

    @Query("SELECT * FROM m_items")
    fun getAllItems(): Flow<List<mItemEntity>>
}