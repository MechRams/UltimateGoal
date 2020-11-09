#ifndef ULTIMATEGOAL_BLOB_DETECTOR_H
#define ULTIMATEGOAL_BLOB_DETECTOR_H

#include <opencv2/core.hpp>
#include <opencv2/features2d.hpp>

using namespace cv;

class SimpleBlobDetector_Context {
public:
    SimpleBlobDetector_Context(const Ptr<SimpleBlobDetector>& detector);
    Ptr<SimpleBlobDetector> detector;
};

#endif //ULTIMATEGOAL_BLOB_DETECTOR_H
