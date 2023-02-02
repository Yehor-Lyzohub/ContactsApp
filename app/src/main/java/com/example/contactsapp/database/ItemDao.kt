package com.example.contactsapp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(item: List<Item>)

    @Update
    suspend fun update(item: Item)

    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: String): Item

    @Query("SELECT * from item ORDER BY isFavorite DESC, firstName ASC")
    fun getItems(): Flow<List<Item>>
}