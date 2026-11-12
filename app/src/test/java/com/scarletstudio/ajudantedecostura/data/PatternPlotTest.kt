package com.scarletstudio.ajudantedecostura.data

import org.junit.Assert.*
import org.junit.Test

class PatternPlotTest {
    @Test fun penUpDoesNotConnectSeparateParts() {
        val plot = PatternPlot.parse("IN;PU0,0;PD4000,0,4000,4000;PU8000,0;PD8000,4000;")
        assertEquals(3, plot.lines.size)
        assertEquals(PlotPoint(8000f, 0f), plot.lines.last().from)
        assertTrue(plot.svg().contains("width=\"200.0mm\""))
        assertTrue(plot.svg().contains("height=\"100.0mm\""))
    }

    @Test fun relativeCoordinatesAndExportKeepGeometry() {
        val plot = PatternPlot.parse("IN;PU100,100;PR;PD400,0,0,800,-400,0;PA;PD100,100;")
        assertEquals(400f, plot.width, 0f)
        assertEquals(800f, plot.height, 0f)
        assertEquals(plot, PatternPlot.parse(plot.plt()))
    }

    @Test fun unsupportedGeometryIsRejectedInsteadOfDropped() {
        assertThrows(IllegalStateException::class.java) {
            PatternPlot.parse("IN;PU0,0;PD400,400;AA100,100,90;")
        }
    }
}
