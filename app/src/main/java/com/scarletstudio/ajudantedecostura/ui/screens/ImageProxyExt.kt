package com.scarletstudio.ajudantedecostura.ui.screens

import androidx.camera.core.ImageProxy
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

fun ImageProxy.toMat(): Mat {
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuv = Mat(height + height / 2, width, CvType.CV_8UC1)
    yuv.put(0, 0, nv21)

    val rgba = Mat()
    Imgproc.cvtColor(yuv, rgba, Imgproc.COLOR_YUV2RGBA_NV21)

    return rgba
}
