package com.example.taskdego.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskdego.data.entity.tItemEntity
import com.example.taskdego.data.entity.mItemEntity
import com.example.taskdego.data.entity.ItemWithQuantity
import kotlinx.coroutines.flow.Flow

@Dao
interface tItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: tItemEntity)

    @Update
    suspend fun updateItem(item: tItemEntity)

    @Query("SELECT * FROM t_items WHERE m_item_id = :itemId LIMIT 1")
    suspend fun getItemByMasterId(itemId: Int): tItemEntity?

    @Query("SELECT * FROM t_items")
    fun getAllItems(): Flow<List<tItemEntity>>

    // ★ 修正：MapではなくItemWithQuantityのリストを返す
    @Query("""
        SELECT 
            m.m_item_id,
            m.item_name,
            m.price,
            m.is_pokemon_candy,
            COALESCE(t.quantity, 0) as quantity
        FROM m_items m 
        LEFT JOIN t_items t ON m.m_item_id = t.m_item_id
        WHERE COALESCE(t.quantity, 0) > 0
        ORDER BY m.m_item_id
    """)
    fun getAllItemsWithQuantity(): Flow<List<ItemWithQuantity>>
}