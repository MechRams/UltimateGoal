package org.firstinspires.ftc.teamcode.cv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class RingPipeline extends OpenCvPipeline {

    static {
        System.loadLibrary("TeamCode");
    }

    @Override
    public void init(Mat input) {
        nativeInitBlobDetector();
    }

    @Override
    public Mat processFrame(Mat input) {

        //PASO 1: Hacer el mat mas pequeño para que quepa en la previsualizacion (podria saltarse este paso en un robot real)
        Mat resizedMat = input;
        //Imgproc.resize(input, resizedMat, new Size(640, 480), 0.0, 0.0, Imgproc.INTER_LINEAR);

        //PASO 2: Difuminar la imagen para eliminar puntos de color y/o elementos que podrian hacer ruido en el resultado final
        Mat blurredMat = new Mat();
        CVGripUtils.cvBoxBlurMat(resizedMat, 2, blurredMat);

        //PASO 3: Convertir el espacio del color del Mat de RGB A YcCrCb
        // (Dos veces con un mat difuminado y uno normal)
        Mat ycbcrBlurredMat = new Mat();
        Imgproc.cvtColor(blurredMat, ycbcrBlurredMat, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(ycbcrBlurredMat, ycbcrBlurredMat, 1);//takes cb difference and stores

        Mat ycbcrMat = new Mat();
        Imgproc.cvtColor(resizedMat, ycbcrMat, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(ycbcrMat, ycbcrMat, 1);//takes cb difference and stores

        Mat hsvBlurredMat = new Mat();
        Imgproc.cvtColor(blurredMat, hsvBlurredMat, Imgproc.COLOR_BGR2HSV);

        Mat hsvMat = new Mat();
        Imgproc.cvtColor(resizedMat, hsvMat, Imgproc.COLOR_BGR2HSV);

        //PASO 4: Clippear los valores ycbcr entre un rango para descartar pixeles
        // (Dos veces con un mat difuminado y uno normal)

        Mat ycbcrBlurredThreshMat = new Mat();
        Imgproc.threshold(ycbcrBlurredMat, ycbcrBlurredThreshMat, 100, 105, Imgproc.THRESH_BINARY_INV);

        Mat ycbcrThreshMat = new Mat();
        Imgproc.threshold(ycbcrMat, ycbcrThreshMat, 100, 105, Imgproc.THRESH_BINARY_INV);

        blurredMat.release();
        ycbcrMat.release();

        //PASO 4.5: Clippear los valores hsv entre un rango para descartar pixeles

        //rangos de hsv
        double[] hue = {8.446981015790435, 43.741098662849254};
        double[] sat = {181.00395338142792, 253.16016738674898};
        double[] val = {115.04061714101688, 247.45084150389948};

        Mat hsvBlurredThreshMat = new Mat();
        Core.inRange(hsvBlurredMat, new Scalar(hue[0], sat[0], val[0]),
                new Scalar(hue[1], sat[1], val[1]), hsvBlurredThreshMat);

        Mat hsvThreshMat = new Mat();
        Core.inRange(hsvMat, new Scalar(hue[0], sat[0], val[0]),
                new Scalar(hue[1], sat[1], val[1]), hsvThreshMat);

        Mat ycbcrBlurredThreshMaskMat = new Mat();
        Mat ycbcrThreshMaskMat = new Mat();

        //Hacer una mascara de los valores clippeados hsv a los valores ycbcr
        CVGripUtils.cvMask(ycbcrBlurredThreshMat, hsvBlurredThreshMat, ycbcrBlurredThreshMaskMat);
        CVGripUtils.cvMask(ycbcrThreshMat, hsvThreshMat, ycbcrThreshMaskMat);

        hsvMat.release();
        hsvBlurredMat.release();
        hsvThreshMat.release();
        hsvBlurredThreshMat.release();

        ycbcrBlurredThreshMat.release();
        ycbcrThreshMat.release();

        //PASO 5: "erosionar" (expandir areas de valor menor) de la imagen difuminada
        Mat erodeMat = new Mat();
        Mat erodeKernel = new Mat();
        Point erodeAnchor = new Point(-1, -1);
        double erodeIterations = 2.0;
        int erodeBordertype = Core.BORDER_CONSTANT;
        Scalar erodeBordervalue = new Scalar(-1);

        CVGripUtils.cvErode(ycbcrBlurredThreshMaskMat, erodeKernel, erodeAnchor, erodeIterations, erodeBordertype, erodeBordervalue, erodeMat);

        ycbcrBlurredMat.release();
        erodeKernel.release();
        ycbcrBlurredThreshMat.release();

        //PASO 6: "dilatar" (expandir areas de valor menor) de la imagen difuminada
        Mat dilateMat = new Mat();
        Mat dilateKernel = new Mat();
        Point dilateAnchor = new Point(-1, -1);
        double dilateIterations = 20.0;
        int dilateBordertype = Core.BORDER_CONSTANT;
        Scalar dilateBordervalue = new Scalar(-1);

        CVGripUtils.cvDilate(erodeMat, dilateKernel, dilateAnchor, dilateIterations, dilateBordertype, dilateBordervalue, dilateMat);

        erodeMat.release();
        dilateKernel.release();

        //PASO 7: Hacer una mascara (recortar partes de un mat) con el mat original recortando
        //las partes blancas de la imagen a la que se le aplicaron los multiples filtros
        Mat maskMat = new Mat();
        CVGripUtils.cvMask(resizedMat, dilateMat, maskMat);

        dilateMat.release();

        //PASO 8: Encontrar los blobs en el mat mascareado para localizar la posicion de la pila de rings
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        nativeDetectBlobs(maskMat.nativeObj, keypoints.nativeObj);



        return input;

    }

    native void nativeInitBlobDetector();

    native void nativeDetectBlobs(long inputPtr, long outputPtr);

}