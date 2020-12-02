package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.teamcode.vision.cv.CvUtils;
import org.firstinspires.ftc.teamcode.vision.cv.NativeBlobDetector;
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

    //rangos de hsv
    double[] hue = {8.446981015790435, 43.741098662849254};
    double[] sat = {181.00395338142792, 253.16016738674898};
    double[] val = {115.04061714101688, 247.45084150389948};

    public Scalar lowerYCrCb = new Scalar(0.0, 141.0, 0.0);
    public Scalar upperYCrCb = new Scalar(255.0, 230.0, 150.0);

    private double aspectRatio;

    private NativeBlobDetector blobDetector;

    @Override
    public void init(Mat input) {

        aspectRatio = (double)input.height() / (double)input.width();

        blobDetector = new NativeBlobDetector(800.0, 0.5, 1.0);

    }

    @Override
    public Mat processFrame(Mat input) {

        aspectRatio = (double)input.height() / (double)input.width();
        double aspectRatioPercentage = aspectRatio / 0.75;

        //PASO 1: Hacer el mat mas pequeño para que quepa en la previsualizacion (podria saltarse este paso en un robot real)
        Mat resizedMat = input;
        //Imgproc.resize(input, resizedMat, new Size(640, 480), 0.0, 0.0, Imgproc.INTER_LINEAR);

        //PASO 2: Difuminar la imagen para eliminar puntos de color y/o elementos que podrian hacer ruido en el resultado final
        Mat blurredMat = new Mat();
        CvUtils.cvBoxBlurMat(resizedMat, 3, blurredMat);

        //PASO 3: Convertir el espacio del color del Mat de RGB A YcCrCb
        // (Dos veces con un mat difuminado y uno normal)
        Mat ycbcrBlurredMat = new Mat();
        Imgproc.cvtColor(blurredMat, ycbcrBlurredMat, Imgproc.COLOR_RGB2YCrCb);

        Mat ycbcrMat = new Mat();
        Imgproc.cvtColor(resizedMat, ycbcrMat, Imgproc.COLOR_RGB2YCrCb);

        Mat hsvBlurredMat = new Mat();
        Imgproc.cvtColor(blurredMat, hsvBlurredMat, Imgproc.COLOR_RGB2HSV);

        Mat hsvMat = new Mat();
        Imgproc.cvtColor(resizedMat, hsvMat, Imgproc.COLOR_RGB2HSV);

        //PASO 4: Clippear los valores hsv y YCbCr entre un rango para descartar pixeles
        // (Dos veces, con un mat difuminado y uno normal)

        Mat ycbcrBlurredThreshMat = new Mat();
        Core.inRange(ycbcrBlurredMat, lowerYCrCb, upperYCrCb, ycbcrBlurredThreshMat);

        Mat ycbcrThreshMat = new Mat();
        Core.inRange(ycbcrBlurredMat, lowerYCrCb, upperYCrCb, ycbcrThreshMat);

        blurredMat.release();
        ycbcrMat.release();

        Mat hsvBlurredThreshMat = new Mat();
        Core.inRange(hsvBlurredMat, new Scalar(hue[0], sat[0], val[0]),
                new Scalar(hue[1], sat[1], val[1]), hsvBlurredThreshMat);

        Mat hsvThreshMat = new Mat();
        Core.inRange(hsvMat, new Scalar(hue[0], sat[0], val[0]),
                new Scalar(hue[1], sat[1], val[1]), hsvThreshMat);

        Mat ycbcrBlurredThreshMaskMat = new Mat();
        Mat ycbcrThreshMaskMat = new Mat();

        CvUtils.cvMask(ycbcrBlurredThreshMat, hsvBlurredThreshMat, ycbcrBlurredThreshMaskMat);
        CvUtils.cvMask(ycbcrThreshMat, hsvThreshMat, ycbcrThreshMaskMat);

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
        double erodeIterations = 1.0;
        int erodeBordertype = Core.BORDER_CONSTANT;
        Scalar erodeBordervalue = new Scalar(-1);

        CvUtils.cvErode(ycbcrBlurredThreshMaskMat, erodeKernel, erodeAnchor, erodeIterations, erodeBordertype, erodeBordervalue, erodeMat);

        ycbcrBlurredMat.release();
        erodeKernel.release();
        ycbcrBlurredThreshMat.release();

        //PASO 6: "dilatar" (expandir areas de valor menor) de la imagen difuminada
        Mat dilateMat = new Mat();
        Mat dilateKernel = new Mat();
        Point dilateAnchor = new Point(-1, -1);
        double dilateIterations = 35.0 * aspectRatioPercentage;
        int dilateBordertype = Core.BORDER_CONSTANT;
        Scalar dilateBordervalue = new Scalar(-1);

        CvUtils.cvDilate(erodeMat, dilateKernel, dilateAnchor, dilateIterations, dilateBordertype, dilateBordervalue, dilateMat);

        erodeMat.release();
        dilateKernel.release();

        //PASO 7: Hacer una mascara (recortar partes de un mat) con el mat original recortando
        //las partes blancas de la imagen a la que se le aplicaron los multiples filtros
        Mat maskMat = new Mat();
        CvUtils.cvMask(resizedMat, dilateMat, maskMat);

        dilateMat.release();

        //PASO 8: Encontrar los blobs en el mat mascareado para localizar la posicion de la pila de rings
        MatOfKeyPoint blobs = blobDetector.detect(maskMat);

        KeyPoint[] blobsArray = blobs.toArray();

        // Si no se detecto ningun blob significa que hay 0 rings
        // Por lo que podemos retornar el resultado en este punto.
        if (blobsArray.length == 0) {

            blobs.release();
            ycbcrThreshMat.release();
            maskMat.release();

            detectedPattern = Pattern.A;

            return resizedMat;

        }

        maskMat.release();

        //PASO 9: Dibujar los blobs en el mat sin filtros
        Mat keypointsMat = new Mat();
        Features2d.drawKeypoints(resizedMat, blobs, keypointsMat, new Scalar(0, 0, 255), Features2d.DrawMatchesFlags_DRAW_RICH_KEYPOINTS);

        //PASO 10: Encontrar los contornos del mat hsv threshold sin difuminar
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        CvUtils.cvFindContours(ycbcrThreshMaskMat, false, contours);

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

                    if (highestYPoint == null) {
                        highestYPoint = point;
                    }
                    if(lowestYPoint == null) {
                        lowestYPoint = point;
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

        if(highestYPoint == null || lowestYPoint == null) return keypointsMat;

        // PASO 13: Calcular el delta de el punto mas abajo y el mas arriba
        // para obtener la altura de los anillos
        double ringsHeight = highestYPoint.y - lowestYPoint.y;

        if(ringsHeight <= 5 * aspectRatioPercentage) {
            detectedPattern = Pattern.A;
        } else if(ringsHeight <= 15 * aspectRatioPercentage) {
            detectedPattern = Pattern.B;
        } else {
            detectedPattern = Pattern.C;
        }

        //Dibujar los contornos en el mat donde se dibujaron los blobs
        Imgproc.drawContours(keypointsMat, contours, -1, new Scalar(0, 255, 0), 2);

        drawTextOutline(keypointsMat, String.format("%.1f", ringsHeight) + " (Stack " + detectedPattern.toString() + ")",
                        new Point(highestYPoint.x, highestYPoint.y - ringsHeight), 0.8, 3, aspectRatioPercentage);

        return keypointsMat;

    }

    private void drawTextOutline(Mat input, String text, Point position, double textSize, double thickness, double aspectRatioPercentage) {

        // Outline
        Imgproc.putText (
                input,
                text,
                position,
                Imgproc.FONT_HERSHEY_PLAIN,
                textSize * aspectRatioPercentage,
                new Scalar(255, 255, 255),
                (int) Math.round(thickness * aspectRatioPercentage)
        );

        //Text
        Imgproc.putText (
                input,
                text,
                position,
                Imgproc.FONT_HERSHEY_PLAIN,
                textSize  * aspectRatioPercentage,
                new Scalar(0, 0, 0),
                (int) Math.round((thickness * aspectRatioPercentage) * (0.4 * aspectRatioPercentage))
        );

    }

    public void destroy() {
        blobDetector.release();
    }

}