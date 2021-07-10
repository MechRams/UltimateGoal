package org.firstinspires.ftc.teamcode.vision

import com.acmerobotics.dashboard.config.Config
import org.firstinspires.ftc.teamcode.vision.RectPipelineConfig.threshold
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline
import kotlin.math.abs
import kotlin.math.roundToInt

class RectRingPipeline : OpenCvPipeline() {

    private val yCrCb = Mat()
    private val cbBottom = Mat()
    private val cbTop = Mat()
    private var top = Mat()
    private var bottom = Mat()

    var topAverage = 0.0
        private set

    var bottomAverage = 0.0
        private set

    override fun processFrame(input: Mat): Mat = RectPipelineConfig.run {
        Imgproc.cvtColor(input, yCrCb, Imgproc.COLOR_RGB2YCrCb)

        //The points needed for the rectangles are calculated here
        val topRect = Rect(
                (yCrCb.width() * topRectWidthPercentage).roundToInt(),
                (yCrCb.height() * topRectHeightPercentage).roundToInt(),
                rectWidth,
                rectHeight
        )

        val bottomRect = Rect(
                (yCrCb.width() * bottomRectWidthPercentage).roundToInt(),
                (yCrCb.height() * bottomRectHeightPercentage).roundToInt(),
                rectWidth,
                rectHeight
        )

        Imgproc.rectangle(input, topRect, Scalar(255.0, 255.0, 0.0), 2)
        Imgproc.rectangle(input, bottomRect, Scalar(255.0, 255.0, 0.0), 2)

        //We crop the image so it is only everything inside the rectangles and find the cb value inside of them
        top = yCrCb.submat(topRect)
        bottom = yCrCb.submat(bottomRect)
        Core.extractChannel(bottom, cbBottom, 2)
        Core.extractChannel(top, cbTop, 2)

        //We take the average
        val bottomMean = Core.mean(cbBottom)
        val topMean = Core.mean(cbTop)

        bottomAverage = bottomMean.`val`[0]
        topAverage = topMean.`val`[0]

        return input
    }

    val detectedHeight: RingHeight get() =
        if (abs(topAverage - bottomAverage) < threshold
                && (topAverage <= 100.0 && bottomAverage <= 100.0)) {
            RingHeight.FOUR
        } else if (abs(topAverage - bottomAverage) < threshold
                && (topAverage >= 100.0 && bottomAverage >= 100.0)) {
            RingHeight.ZERO
        } else {
            RingHeight.ONE
        }

}

@Config
object RectPipelineConfig {
    @JvmField var topRectWidthPercentage = 0.1
    @JvmField var topRectHeightPercentage = 0.6
    @JvmField var bottomRectWidthPercentage = 0.10
    @JvmField var bottomRectHeightPercentage = 0.68

    @JvmField var rectWidth = 10
    @JvmField var rectHeight = 10

    @JvmField var threshold = 15
}