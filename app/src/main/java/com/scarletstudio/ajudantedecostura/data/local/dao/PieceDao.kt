package com.scarletstudio.ajudantedecostura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.scarletstudio.ajudantedecostura.data.local.entity.PieceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PieceDao {

    @Insert
    suspend fun insert(piece: PieceEntity)

    @Update
    suspend fun update(piece: PieceEntity)

    @Delete
    suspend fun delete(piece: PieceEntity)

    @Query("SELECT * FROM pieces ORDER BY date DESC")
    fun getAll(): Flow<List<PieceEntity>>

    @Query("DELETE FROM pieces")
    suspend fun deleteAll()
}
