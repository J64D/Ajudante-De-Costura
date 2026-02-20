package com.scarletstudio.ajudantedecostura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pieces")
data class PieceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: String,
    val size: String,
    val quantity: Int,
    val date: Long = System.currentTimeMillis()
)
