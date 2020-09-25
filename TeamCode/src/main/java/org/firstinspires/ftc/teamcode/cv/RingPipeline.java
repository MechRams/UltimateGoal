package org.firstinspires.ftc.teamcode.cv;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class RingPipeline extends OpenCvPipeline {

    public enum Pattern { A, B, C, ND }

    public volatile Pattern detectedPattern = Pattern.ND;

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
        MatOfKeyPoint blobs = new MatOfKeyPoint();
        nativeDetectBlobs(maskMat.nativeObj, blobs.nativeObj);

        KeyPoint[] blobsArray = blobs.toArray();

        // Si no se detecto ningun blob significa que hay 0 rings
        // Por lo que podemos retornar el resultado en este punto.
        if (blobsArray.length == 0) {
            blobs.release();
            ycbcrThreshMat.release();
            maskMat.release();
            detectedPattern = Pattern.A;
            return resizedMat.clone();
        }

        //PASO 9: Dibujar los blobs en el mat sin filtros
        Mat keypointsMat = new Mat();
        Features2d.drawKeypoints(resizedMat, blobs, keypointsMat, new Scalar(0, 0, 255), Features2d.DrawMatchesFlags_DRAW_RICH_KEYPOINTS);

        //PASO 10: Encontrar los contornos del mat hsv threshold sin difuminar
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        CVGripUtils.cvFindContours(ycbcrThreshMaskMat, false, contours);

        //PASO 11: Encontrar el blob mas grande en la imagen (por si se detectaron varios)
        KeyPoint biggestKeyPoint = null;
        for (KeyPoint keyPoint : blobsArray) {

            if (biggestKeyPoint == null) {
                biggestKeyPoint = keyPoint;
                continue;
            }

            if (keyPoint.size > biggestKeyPoint.size) {
                biggestKeyPoint = keyPoint;
            }

        }

        blobs.release();

        //PASO 12: Buscar los contours que estan dentro del blob mas grande
        //y luego encontrar el punto mas bajo y el mas alto del contorno
        //de los anillos para calcular su altura

        Point highestYPoint = null;
        Point lowestYPoint = null;

        double circleX = biggestKeyPoint.pt.x;
        double circleY = biggestKeyPoint.pt.y;

        double circleRadius = biggestKeyPoint.size / 2;

        double circleRadiusPow = Math.pow(circleRadius, 2);

        for (MatOfPoint contour : contours) {

            for (Point point : contour.toArray()) {

                double cx = point.x;
                double cy = point.y;

                if (Math.pow(cx - circleX, 2) + Math.pow(cy - circleY, 2) < circleRadiusPow) {

                    if (highestYPoint == null && lowestYPoint == null) {
                        highestYPoint = point;
                        lowestYPoint = point;
                        continue;
                    }

                    if (point.y > highestYPoint.y) {
                        highestYPoint = point;
                    }

                    if (point.y < lowestYPoint.y) {
                        lowestYPoint = point;
                    }

                }

            }

        }

        // PASO 13: Calcular el delta de el punto mas abajo y el mas arriba
        // para obtener la altura de los anillos
        double ringsHeight = highestYPoint.y - lowestYPoint.y;

        if(ringsHeight <= 5) {
            detectedPattern = Pattern.A;
        } else if(ringsHeight <= 15) {
            detectedPattern = Pattern.B;
        } else {
            detectedPattern = Pattern.C;
        }

        //Dibujar los contornos en el mat donde se dibujaron los blobs
        Imgproc.drawContours(keypointsMat, contours, -1, new Scalar(0, 255, 0), 2);

        return keypointsMat;

    }

    native void nativeInitBlobDetector();

    native void nativeDetectBlobs(long inputPtr, long outputPtr);

}