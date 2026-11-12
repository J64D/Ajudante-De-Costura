package com.scarletstudio.ajudantedecostura.data

data class PlotPoint(val x: Float, val y: Float)
data class PlotLine(val from: PlotPoint, val to: PlotPoint)
data class PatternPlot(val lines: List<PlotLine>) {
    fun plt(): String = buildString {
        append("IN;SP1;")
        lines.forEach { append("PU${it.from.x},${it.from.y};PD${it.to.x},${it.to.y};") }
        append("SP0;")
    }
    val minX = lines.minOf { minOf(it.from.x, it.to.x) }
    val minY = lines.minOf { minOf(it.from.y, it.to.y) }
    val width = lines.maxOf { maxOf(it.from.x, it.to.x) } - minX
    val height = lines.maxOf { maxOf(it.from.y, it.to.y) } - minY
    fun svg(): String = buildString {
        // HP-GL default plotter unit: 0.025 mm. No rescaling of geometry.
        append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"${width / 40}mm\" height=\"${height / 40}mm\" viewBox=\"0 0 $width $height\">")
        append("<g fill=\"none\" stroke=\"black\" stroke-width=\"8\">")
        lines.forEach { append("<path d=\"M${it.from.x - minX},${height - (it.from.y - minY)} L${it.to.x - minX},${height - (it.to.y - minY)}\"/>") }
        append("</g></svg>")
    }
    companion object {
        fun parse(text: String): PatternPlot {
            require(text.length <= 5 * 1024 * 1024) { "Arquivo muito grande (máximo 5 MB)." }
            var current = PlotPoint(0f, 0f)
            var down = false
            var absolute = true
            val lines = mutableListOf<PlotLine>()
            text.split(';').forEach { raw ->
                val command = raw.trim()
                if (command.isEmpty()) return@forEach
                require(command.length >= 2) { "Comando PLT inválido." }
                val op = command.take(2).uppercase()
                val args = command.drop(2).trim()
                when (op) {
                    "IN" -> { current = PlotPoint(0f, 0f); down = false; absolute = true }
                    "SP", "QL", "PG" -> Unit
                    "LT" -> require(args.isEmpty()) { "Estilo de linha não suportado." }
                    "PU", "PD", "PA", "PR" -> {
                        if (op == "PU") down = false
                        if (op == "PD") down = true
                        if (op == "PA") absolute = true
                        if (op == "PR") absolute = false
                        if (args.isNotEmpty()) {
                            val values = args.split(Regex("[,\\s]+")).map { it.toFloat() }
                            require(values.size % 2 == 0 && values.all { it.isFinite() && kotlin.math.abs(it) <= 1000000 }) { "Coordenadas inválidas." }
                            values.chunked(2).forEach { pair ->
                                val next = PlotPoint(pair[0] + if (absolute) 0f else current.x, pair[1] + if (absolute) 0f else current.y)
                                require(kotlin.math.abs(next.x) <= 1000000 && kotlin.math.abs(next.y) <= 1000000)
                                if (down && next != current) lines.add(PlotLine(current, next))
                                current = next
                            }
                        }
                    }
                    else -> error("Comando $op não suportado. O arquivo não foi importado para evitar um molde incompleto.")
                }
                require(lines.size <= 50000) { "O arquivo tem traços demais." }
            }
            require(lines.isNotEmpty()) { "Nenhum traçado encontrado." }
            return PatternPlot(lines).also { require(it.width > 0 && it.height > 0) }
        }
    }
}
