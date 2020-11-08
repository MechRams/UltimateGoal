package org.firstinspires.ftc.teamcode.vision.cv;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class CVGripUtils {

    /**
     * Softens an image using a box filter.
     * @param input The image on which to perform the blur.
     * @param doubleRadius The radius for the blur.
     * @param output The image in which to store the output.
     */
    public static void cvBoxBlurMat(Mat input, double doubleRadius, Mat output) {
        int radius = (int)(doubleRadius + 0.5);
        int kernelSize = 2 * radius + 1;
        Imgproc.blur(input, output, new Size(kernelSize, kernelSize));
    }

    /**
     * Expands area of lower value in an image.
     * @param src the Image to erode.
     * @param kernel the kernel for erosion.
     * @param anchor the center of the kernel.
     * @param iterations the number of times to perform the erosion.
     * @param borderType pixel extrapolation method.
     * @param borderValue value to be used for a constant border.
     * @param dst Output Image.
     */
    public static void cvErode(Mat src, Mat kernel, Point anchor, double iterations,
                               int borderType, Scalar borderValue, Mat dst) {
        if (kernel == null) {
            kernel = new Mat();
        }
        if (anchor == null) {
            anchor = new Point(-1,-1);
        }
        if (borderValue == null) {
            borderValue = new Scalar(-1);
        }
        Imgproc.erode(src, dst, kernel, anchor, (int)iterations, borderType, borderValue);
    }

    /**
     * Expands area of higher value in an image.
     * @param src the Image to dilate.
     * @param kernel the kernel for dilation.
     * @param anchor the center of the kernel.
     * @param iterations the number of times to perform the dilation.
     * @param borderType pixel extrapolation method.
     * @param borderValue value to be used for a constant border.
     * @param dst Output Image.
     */
    public static void cvDilate(Mat src, Mat kernel, Point anchor, double iterations,
                                int borderType, Scalar borderValue, Mat dst) {
        if (kernel == null) {
            kernel = new Mat();
        }
        if (anchor == null) {
            anchor = new Point(-1,-1);
        }
        if (borderValue == null){
            borderValue = new Scalar(-1);
        }
        Imgproc.dilate(src, dst, kernel, anchor, (int)iterations, borderType, borderValue);
    }

    /**
     * Filters in an area of an image using an inverse binary mask.
     * @param input The image on which the mask filters.
     * @param mask The binary image that is used to filter.
     * @param output The image in which to store the output.
     */
    public static void cvInvertedMask(Mat input, Mat mask, Mat output) {
        Mat cMask = mask.clone();
        mask.convertTo(cMask, CvType.CV_8UC1);
        Core.bitwise_not(cMask, cMask);
        Core.bitwise_xor(output, output, output);
        input.copyTo(output, cMask);
        cMask.release();
    }

    /**
     * Filter out an area of an image using a binary mask.
     * @param input The image on which the mask filters.
     * @param mask The binary image that is used to filter.
     * @param output The image in which to store the output.
     */
    public static void cvMask(Mat input, Mat mask, Mat output) {
        mask.convertTo(mask, CvType.CV_8UC1);
        Core.bitwise_xor(output, output, output);
        input.copyTo(output, mask);
    }

    public static void cvFindContours(Mat input, boolean externalOnly, List<MatOfPoint> contours) {

        Mat hierarchy = new Mat();
        contours.clear();

        int mode;
        if (externalOnly) {
            mode = Imgproc.RETR_EXTERNAL;
        } else {
            mode = Imgproc.RETR_LIST;
        }

        int method = Imgproc.CHAIN_APPROX_SIMPLE;
        Imgproc.findContours(input, contours, hierarchy, mode, method);

    }

}