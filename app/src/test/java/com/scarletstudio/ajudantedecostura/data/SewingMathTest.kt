package com.scarletstudio.ajudantedecostura.data

import org.junit.Assert.*
import org.junit.Test

class SewingMathTest {
    @Test fun supportsPortugueseDecimals() {
        assertEquals(91.65, SewingMath.price("20,50", "2,5", "20", "30")!!, .001)
        assertEquals(40.0, SewingMath.elasticity("14")!!, .001)
        assertEquals(0.0, SewingMath.elasticity("10")!!, .001)
    }
    @Test fun rejectsInvalidNumbers() {
        listOf("", "NaN", "Infinity", "-1", "1,2,3", "1.2.3").forEach { assertNull(SewingMath.decimal(it)) }
        assertNull(SewingMath.elasticity("9"))
        assertNull(SewingMath.elasticity("101"))
        assertNull(SewingMath.price("20", "", "20", "30"))
        assertNull(SewingMath.price("20", "2", "20", "1001"))
    }
}
