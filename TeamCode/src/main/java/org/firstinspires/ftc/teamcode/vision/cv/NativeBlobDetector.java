package org.firstinspires.ftc.teamcode.vision.cv;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.util.RobotLog;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

public class NativeBlobDetector {

    static {
        System.loadLibrary("TeamCode");
    }

    long nativeObj;

    public NativeBlobDetector(double minArea, double minCircularity, double maxCircularity) {
        nativeObj = nativeCreateBlobDetector(minArea, minCircularity, maxCircularity);
    }

    public MatOfKeyPoint detect(Mat input) {
        MatOfKeyPoint keyPoints = new MatOfKeyPoint();
        nativeDetectBlobs(nativeObj, input.nativeObj, keyPoints.nativeObj);
        return keyPoints;
    }

    public void release() {
        nativeReleaseBlobDetector(nativeObj);
    }

    @Override
    public void finalize() {
        release();
    }

    native long nativeCreateBlobDetector(double minArea, double minCircularity, double maxCircularity);

    native void nativeDetectBlobs(long blobDetPtr, long inputPtr, long outputPtr);

    native void nativeReleaseBlobDetector(long blobDetPtr);

}
