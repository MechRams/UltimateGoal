#include <limits>

#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/features2d.hpp>

using namespace cv;
using namespace std;

Ptr<SimpleBlobDetector> detector;

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
JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_vision_RingPipeline_nativeInitBlobDetector(JNIEnv *env, jobject thiz) {

    SimpleBlobDetector::Params params;

    params.thresholdStep = 10;
    params.minThreshold = 50;
    params.maxThreshold = 220;

    params.minRepeatability = 2;
    params.minDistBetweenBlobs = 10;

    params.filterByColor = true;
    params.blobColor = 255;

    params.filterByArea = true;
    params.minArea = 800;
    params.maxArea = imax;

    params.filterByCircularity = true;
    params.minCircularity = 0.5084745762711864;
    params.maxCircularity = 1.0;

    params.filterByInertia = true;
    params.minInertiaRatio = 0.1;
    params.maxInertiaRatio = imax;

    params.filterByConvexity = true;
    params.minConvexity = 0.95;
    params.maxConvexity = imax;

    detector = SimpleBlobDetector::create(params);

}

extern "C"
JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_vision_RingPipeline_nativeDetectBlobs(JNIEnv *env, jobject thiz, jlong inputPtr, jlong outputPtr) {

    Mat* input = (cv::Mat*) inputPtr;
    Mat* output = (cv::Mat*) outputPtr;

    vector<KeyPoint> keyPoints = vector<KeyPoint>();

    detector->detect(*input, keyPoints);

    vecKeyPointToMat(keyPoints, *output);

}