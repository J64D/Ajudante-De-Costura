package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import kotlin.math.max

class FabricEdgeAnalyzer(
    private val onShapeDetected: (
        points: List<Point>,
        imageWidth: Int,
        imageHeight: Int
    ) -> Unit
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {

        try {
            /* ================= CONVERSÃO ================= */

            var mat = image.toMat()

            /* ================= ROTAÇÃO ================= */

            when (image.imageInfo.rotationDegrees) {
                90 -> Core.rotate(mat, mat, Core.ROTATE_90_CLOCKWISE)
                180 -> Core.rotate(mat, mat, Core.ROTATE_180)
                270 -> Core.rotate(mat, mat, Core.ROTATE_90_COUNTERCLOCKWISE)
            }

            /* ================= REDUÇÃO (PERFORMANCE) ================= */

            val maxSize = 640.0
            val scale = maxSize / max(mat.width(), mat.height())

            if (scale < 1.0) {
                Imgproc.resize(
                    mat,
                    mat,
                    Size(mat.width() * scale, mat.height() * scale),
                    0.0,
                    0.0,
                    Imgproc.INTER_AREA
                )
            }

            val processedWidth = mat.width()
            val processedHeight = mat.height()

            /* ================= PREPROCESS ================= */

            val gray = Mat()
            Imgproc.cvtColor(mat, gray, Imgproc.COLOR_RGBA2GRAY)

            Imgproc.GaussianBlur(gray, gray, Size(7.0, 7.0), 0.0)

            val thresh = Mat()
            Imgproc.adaptiveThreshold(
                gray,
                thresh,
                255.0,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY_INV,
                21,
                5.0
            )

            val kernel = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                Size(5.0, 5.0)
            )
            Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, kernel)

            /* ================= CONTORNOS ================= */

            val contours = mutableListOf<MatOfPoint>()
            Imgproc.findContours(
                thresh,
                contours,
                Mat(),
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE
            )

            val biggest = contours
                .filter { Imgproc.contourArea(it) > 8_000 }
                .maxByOrNull { Imgproc.contourArea(it) }

            /* ================= APROXIMAÇÃO ================= */

            biggest?.let {
                val approx = MatOfPoint2f()
                Imgproc.approxPolyDP(
                    MatOfPoint2f(*it.toArray()),
                    approx,
                    10.0,
                    true
                )

                onShapeDetected(
                    approx.toList(),
                    processedWidth,
                    processedHeight
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            image.close() // 🔥 OBRIGATÓRIO
        }
    }
}
