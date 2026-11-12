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
    @Query("UPDATE pieces SET quantity = quantity + :delta WHERE id = :id AND quantity + :delta BETWEEN 1 AND 99999")
    suspend fun adjustQuantity(id: Int, delta: Int)

    @Query("SELECT * FROM pieces WHERE id = :id")
    suspend fun find(id: Int): PieceEntity?

    @Insert
    suspend fun insertAll(pieces: List<PieceEntity>)

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
