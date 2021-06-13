package org.firstinspires.ftc.teamcode.vision

import org.firstinspires.ftc.teamcode.vision.cv.CvUtils
import org.firstinspires.ftc.teamcode.vision.cv.NativeBlobDetector
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.round

class RingPipeline2 : OpenCvPipeline() {

    /** enum class for Height of the ring **/
    enum class RingHeight {
        ZERO, ONE, FOUR
    }

    /** enum class for Height of the stone **/
    enum class MostLikelyMode {
        CENTER_ML, LEFT_ML, RIGHT_ML
    }

    data class RingStack(val rect: Rect, val ringHeight: RingHeight, val aspectRatio: Double, val distanceFromCenter: Point)

    /** companion object to store all static variables needed **/
    companion object Config {

        /** values used for inRange calculation
         * set to var in-case user wants to use their own tuned values
         * stored in YCrCb format **/
        var lowerYCrCb = Scalar(0.0, 141.0, 0.0)
        var upperYCrCb = Scalar(255.0, 230.0, 150.0)

        /** values used for inRange calculation
         * set to var in-case user wants to use their own tuned values
         * stored in HSV format **/
        var lowerHsv = Scalar(8.44, 181.003, 115.0406)
        var upperHsv = Scalar(43.74, 253.1601, 247.4508)

        var mostLikelyMode = MostLikelyMode.CENTER_ML

    }

    /** variable to store the detected stacks **/
    private val detectedStacks: ArrayList<RingStack> = ArrayList()
    @Volatile private var latestMostLikelyStack: RingStack? = null

    private var blobDet = NativeBlobDetector(800.0, 0.7, 1.0)
    val ret = Mat()

    val ycbcrMat = Mat() // variable to store ycbcr mask in
    val hsvMat = Mat() // variable to store hsv mask in

    val ycbcrMask = Mat() // variable to store ycbcr mask in
    val hsvMask = Mat() // variable to store hsv mask in

    val mask = Mat()

    val hierarchy = Mat()
    val kernel = Mat()

    val erodeMat = Mat()
    val erodeDilateMask = Mat()

    override fun processFrame(input: Mat): Mat {

        detectedStacks.clear()

        try { // try catch in order for opMode to not crash and force a restart

            /**converting from RGB color space to YCrCb color space**/
            Imgproc.cvtColor(input, ycbcrMat, Imgproc.COLOR_RGB2YCrCb)
            Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV)

            /**checking if any pixel is within the orange bounds to make a black and white mask**/
            Core.inRange(ycbcrMat, lowerYCrCb, upperYCrCb, ycbcrMask)
            Core.inRange(hsvMat, lowerHsv, upperHsv, hsvMask)

            ycbcrMat.release()
            hsvMat.release()

            CvUtils.cvMask(ycbcrMask, hsvMask, mask)

            ycbcrMask.release()
            hsvMask.release()

            /**applying to input and putting it on ret in black or yellow**/
            CvUtils.cvMask(input, mask, ret)

            ycbcrMask.release()
            hsvMask.release()

            /**applying box blur to reduce noise when finding contours**/
            CvUtils.cvBoxBlurMat(mask, 1.0, mask)

            /**erode then dilate to improve results**/
            val anchor = Point(-1.0, -1.0)
            val borderType = Core.BORDER_CONSTANT
            val borderValue = Scalar(-1.0)

            val erodeIterations = 1.0
            CvUtils.cvErode(mask, kernel, anchor, erodeIterations, borderType, borderValue, erodeMat)

            val dilateIterations = 12.0
            CvUtils.cvDilate(erodeMat, kernel, anchor, dilateIterations, borderType, borderValue, erodeDilateMask)

            erodeMat.release()
            kernel.release()

            /**finding blobs in dilated Mask**/
            val blobs = blobDet.detect(erodeDilateMask)

            erodeDilateMask.release()

            //we found no blobs, meaning there aren't any rings in the picture
            //we exit the function here to save resources.
            if(blobs.toArray().isEmpty()) {
                blobs.release()
                return input
            }

            val centerX = input.width().toDouble() / 2.0
            val centerY = input.height().toDouble() / 2.0

            val aspectRatio = input.height().toDouble() / input.width().toDouble()

            /**finding contours on mask**/
            val contours: List<MatOfPoint> = ArrayList()

            Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE)

            hierarchy.release()

            for(keyPoint in blobs.toArray()) {

                val circleX: Double = keyPoint!!.pt.x
                val circleY: Double = keyPoint.pt.y

                val circleRadius: Double = keyPoint.size / 2.0
                val circleRadiusPow = circleRadius.pow(2.0)

                val contoursInBlob: ArrayList<Point> = ArrayList()

                for (contour in contours) {
                    for (point in contour.toArray()) {
                        val cx = point.x
                        val cy = point.y
                        if ((cx - circleX).pow(2.0) + (cy - circleY).pow(2.0) < circleRadiusPow) {
                            contoursInBlob.add(point)
                        }
                    }
                }

                val contoursInBlobMat = MatOfPoint()
                contoursInBlobMat.fromList(contoursInBlob)

                val rect: Rect = Imgproc.boundingRect(contoursInBlobMat)
                Imgproc.rectangle(input, rect, Scalar(0.0, 0.0, 255.0), 2)

                contoursInBlobMat.release()

                val rectCenterX = rect.x - rect.width / 2
                val rectCenterY = rect.y - rect.height / 2

                val rectAspectRatio = rect.height.toDouble() / rect.width.toDouble()

                val ringHeight = when {
                    rectAspectRatio >= 0.5 -> {
                        RingHeight.FOUR
                    }
                    rectAspectRatio >= 0.2 -> {
                        RingHeight.ONE
                    }
                    else -> {
                        RingHeight.ZERO
                    }
                }

                detectedStacks.add(RingStack(rect, ringHeight, rectAspectRatio, Point(centerX - rectCenterX, centerY - rectCenterY)))

            }

            blobs.release()

            for (contour in contours) {
                contour.release()
            }

            val mlStack = getMostLikelyStack()

            if(mlStack != null) {
                drawTextOutline(input, "|>" + mlStack.ringHeight.name, Point(mlStack.rect.x.toDouble(), mlStack.rect.y.toDouble()),
                        0.8, 3.0, aspectRatio)
            }

            for(stack in detectedStacks) {
                if(stack != mlStack) {
                    drawTextOutline(input, stack.ringHeight.name, Point(stack.rect.x.toDouble(), stack.rect.y.toDouble()),
                            0.8, 3.0, aspectRatio)
                }
            }

            mask.release()
            ret.release()

        } catch (e: Exception) {
            /**error handling, prints stack trace for specific debug**/
            //telemetry?.addData("[ERROR]", e)
            //e.stackTrace.toList().stream().forEach { x -> telemetry?.addLine(x.toString()) }
            e.printStackTrace()
        }

        return input

    }

    private fun drawTextOutline(input: Mat, text: String, position: Point, textSize: Double, thickness: Double, aspectRatioPercentage: Double) {

        // Outline
        Imgproc.putText(
                input,
                text,
                position,
                Imgproc.FONT_HERSHEY_PLAIN,
                textSize * aspectRatioPercentage,
                Scalar(255.0, 255.0, 255.0),
                round(thickness * aspectRatioPercentage).toInt()
        )

        //Text
        Imgproc.putText(
                input,
                text,
                position,
                Imgproc.FONT_HERSHEY_PLAIN,
                textSize * aspectRatioPercentage,
                Scalar(0.0, 0.0, 0.0),
                round(thickness * aspectRatioPercentage * (0.2 * aspectRatioPercentage)).toInt()
        )

    }

    fun getDetectedStacks() = detectedStacks.toTypedArray()

    fun getMostLikelyStack() : RingStack? {

        var mlStack: RingStack? = null

        for(stack in getDetectedStacks()) {

            if(mlStack == null) {
                mlStack = stack
                continue
            } else if(stack.aspectRatio > 0.90 || stack.ringHeight == RingHeight.ZERO) {
                continue
            }

            when(mostLikelyMode) {

                MostLikelyMode.CENTER_ML -> {
                    val magCurrent = hypot(mlStack.distanceFromCenter.x, mlStack.distanceFromCenter.y)
                    val magNew = hypot(stack.distanceFromCenter.x, stack.distanceFromCenter.y)

                    if (magNew < magCurrent) mlStack = stack
                }

                MostLikelyMode.RIGHT_ML -> {
                    if (stack.rect.x < mlStack.rect.x)
                        mlStack = stack
                }

                MostLikelyMode.LEFT_ML -> {
                    if (stack.rect.x > mlStack.rect.x)
                        mlStack = stack
                }

            }

        }

        latestMostLikelyStack = mlStack

        return mlStack

    }

    fun getMostLikelyHeight() : RingHeight = getMostLikelyStack()?.ringHeight ?: RingHeight.ZERO

    fun getLatestMostLikelyStack() : RingStack? = latestMostLikelyStack

    fun getLatestMostLikelyHeight() : RingHeight = getLatestMostLikelyStack()?.ringHeight ?: RingHeight.ZERO

    fun destroy() {
        blobDet.release()
    }

}