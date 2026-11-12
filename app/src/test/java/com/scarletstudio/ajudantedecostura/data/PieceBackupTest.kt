package com.scarletstudio.ajudantedecostura.data

import com.scarletstudio.ajudantedecostura.data.local.PieceBackup
import com.scarletstudio.ajudantedecostura.data.local.entity.PieceEntity
import org.junit.Assert.*
import org.junit.Test

class PieceBackupTest {
    @Test fun preservesContentWithoutReusingDatabaseIds() {
        val original = PieceEntity(8, "Vestido ç", "Rosa", "M", 4, 123456L)
        assertEquals(original.copy(id = 0), PieceBackup.decode(PieceBackup.encode(listOf(original))).single())
        assertTrue(PieceBackup.decode(PieceBackup.encode(emptyList())).isEmpty())
    }
    @Test fun rejectsCorruptBackupBeforeInsertingAnything() {
        val valid = PieceBackup.encode(listOf(PieceEntity(name = "Saia", color = "Rosa", size = "G", quantity = 2)))
        listOf("{}", "not-json", valid.replace("\"quantity\": 2", "\"quantity\": -1"),
            valid.replace("\"quantity\": 2", "\"quantity\": 1.5"),
            valid.replace("\"version\": 1", "\"version\": 99")).forEach {
            assertTrue(it, runCatching { PieceBackup.decode(it) }.isFailure)
        }
    }
}
