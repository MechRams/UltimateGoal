#include <limits>

#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/features2d.hpp>

using namespace cv;
using namespace std;

int imax = std::numeric_limits<int>::max();

void vecKeyPointToMat(vector<KeyPoint>& v_kp, Mat& mat) {

    int count = (int)v_kp.size();
    mat.create(count, 1, CV_32FC(7));

    for(int i=0; i<count; i++) {
        KeyPoint kp = v_kp[i];
        mat.at< Vec<float, 7> >(i, 0) = Vec<float, 7>(kp.pt.x, kp.pt.y, kp.size, kp.angle, kp.response, (float)kp.octave, (float)kp.class_id);
    }

}

extern "C"
JNIEXPORT jlong JNICALL
Java_org_firstinspires_ftc_teamcode_vision_cv_NativeBlobDetector_nativeCreateBlobDetector(JNIEnv *env, jobject thiz, jdouble minArea, jdouble minCircularity, jdouble maxCircularity) {

    SimpleBlobDetector::Params params;

    params.thresholdStep = 10;
    params.minThreshold = 50;
    params.maxThreshold = 220;

    params.minRepeatability = 2;
    params.minDistBetweenBlobs = 10;

    params.filterByColor = true;
    params.blobColor = 255;

    params.filterByArea = true;
    params.minArea = minArea;
    params.maxArea = imax;

    params.filterByCircularity = true;
    params.minCircularity = minCircularity;
    params.maxCircularity = maxCircularity;

    params.filterByInertia = true;
    params.minInertiaRatio = 0.1;
    params.maxInertiaRatio = imax;

    params.filterByConvexity = true;
    params.minConvexity = 0.95;
    params.maxConvexity = imax;

    SimpleBlobDetector *blobDet = SimpleBlobDetector::create(params);

    blobDet = static_cast<SimpleBlobDetector*> (malloc(sizeof(&blobDet)));

    return (long)blobDet;

}

extern "C"
JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_vision_cv_NativeBlobDetector_nativeDetectBlobs(JNIEnv *env, jobject thiz, jlong blob_det_ptr, jlong input_ptr, jlong output_ptr) {

    auto* blob_det = (SimpleBlobDetector*) blob_det_ptr;

    Mat* input = (cv::Mat*) input_ptr;
    Mat* output = (cv::Mat*) output_ptr;

    vector<KeyPoint> keyPoints = vector<KeyPoint>();

    blob_det->detect(*input, keyPoints);

    vecKeyPointToMat(keyPoints, *output);

}

extern "C"
JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_vision_cv_NativeBlobDetector_nativeReleaseBlobDetector(JNIEnv *env, jobject thiz, jlong blob_det_ptr) {
    auto* blob_det = (SimpleBlobDetector*) blob_det_ptr;
    blob_det->clear();
    delete blob_det;
}