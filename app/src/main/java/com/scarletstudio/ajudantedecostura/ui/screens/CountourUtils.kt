
package com.scarletstudio.ajudantedecostura.ui.screens
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * Extrai o MAIOR contorno detectado (normalmente o tecido)
 */
fun extractMainContour(edges: Mat): List<Point> {

    val contours = mutableListOf<MatOfPoint>()
    val hierarchy = Mat()

    Imgproc.findContours(
        edges,
        contours,
        hierarchy,
        Imgproc.RETR_EXTERNAL,
        Imgproc.CHAIN_APPROX_SIMPLE
    )

    if (contours.isEmpty()) return emptyList()

    // 🔥 pega o maior contorno por área
    val biggest = contours.maxByOrNull {
        Imgproc.contourArea(it)
    } ?: return emptyList()

    // 🔧 suaviza o contorno (remove tremido)
    val approx = MatOfPoint2f()
    Imgproc.approxPolyDP(
        MatOfPoint2f(*biggest.toArray()),
        approx,
        8.0,
        true
    )

    return approx.toList()
}

