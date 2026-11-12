package com.scarletstudio.ajudantedecostura.data

object SewingMath {
    fun decimal(text: String): Double? = text.trim().replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() && it >= 0 }
    fun price(material: String, hours: String, rate: String, markup: String): Double? {
        val m = decimal(material) ?: return null
        val h = decimal(hours) ?: return null
        val r = decimal(rate) ?: return null
        val p = decimal(markup) ?: return null
        if (m > 1000000 || h > 100000 || r > 1000000 || p > 1000) return null
        return ((m + h * r) * (1 + p / 100)).takeIf { it.isFinite() }
    }
    fun elasticity(stretched: String): Double? = decimal(stretched)?.takeIf { it in 10.0..100.0 }?.let { (it - 10) * 10 }
}
