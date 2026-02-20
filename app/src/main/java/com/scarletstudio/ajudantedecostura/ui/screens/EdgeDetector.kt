package com.scarletstudio.ajudantedecostura.ui.screens

import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

object EdgeDetector {

    fun detectEdges(src: Mat): Mat {
        val gray = Mat()
        val blurred = Mat()
        val edges = Mat()

        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY)

        Imgproc.GaussianBlur(
            gray,
            blurred,
            Size(5.0, 5.0),
            0.0
        )

        Imgproc.Canny(
            blurred,
            edges,
            80.0,
            160.0
        )

        return edges
    }
}
