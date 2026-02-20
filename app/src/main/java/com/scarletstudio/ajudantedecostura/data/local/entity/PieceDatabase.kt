package com.scarletstudio.ajudantedecostura.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.scarletstudio.ajudantedecostura.data.local.dao.PieceDao
import com.scarletstudio.ajudantedecostura.data.local.entity.PieceEntity

@Database(
    entities = [PieceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PieceDatabase : RoomDatabase() {

    abstract fun pieceDao(): PieceDao

    companion object {
        @Volatile
        private var INSTANCE: PieceDatabase? = null

        fun getDatabase(context: Context): PieceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PieceDatabase::class.java,
                    "piece_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
