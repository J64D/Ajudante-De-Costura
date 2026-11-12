package com.scarletstudio.ajudantedecostura.data.local

import com.scarletstudio.ajudantedecostura.data.local.entity.PieceEntity
import org.json.JSONArray
import org.json.JSONObject

object PieceBackup {
    fun encode(pieces: List<PieceEntity>): String = JSONObject().put("version", 1).put("pieces", JSONArray().apply {
        pieces.forEach { piece -> put(JSONObject().put("name", piece.name).put("color", piece.color)
            .put("size", piece.size).put("quantity", piece.quantity).put("date", piece.date)) }
    }).toString(2)

    fun decode(text: String): List<PieceEntity> {
        val root = JSONObject(text)
        require(root.getInt("version") == 1) { "Versão de backup não suportada." }
        val rows = root.getJSONArray("pieces")
        require(rows.length() <= 10000) { "O arquivo excede 10.000 registros." }
        return (0 until rows.length()).map { index ->
            val row = rows.getJSONObject(index)
            val name = row.getString("name").trim()
            val color = row.getString("color").trim()
            val size = row.getString("size").trim()
            val rawQuantity = row.get("quantity")
            require(rawQuantity is Number && rawQuantity.toDouble() == rawQuantity.toInt().toDouble())
            val quantity = rawQuantity.toInt()
            val date = row.getLong("date")
            require(name.isNotBlank() && name.length <= 120 && color.length <= 80 && size.length <= 40 && quantity in 1..99999 && date >= 0)
            PieceEntity(name = name, color = color, size = size, quantity = quantity, date = date)
        }
    }
}
