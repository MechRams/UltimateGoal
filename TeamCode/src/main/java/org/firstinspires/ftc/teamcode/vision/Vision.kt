package org.firstinspires.ftc.teamcode.vision

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.*

open class Vision(private val hardwareMap: HardwareMap) {

    var phoneCam: OpenCvCamera? = null

    var ringPipeline: RingPipeline? = null
    var ringPipeline2: RingPipeline2? = null
    var rectRingPipeline: RectRingPipeline? = null

    val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier(
            "cameraMonitorViewId", "id", hardwareMap.appContext.packageName
    )

    fun initWebcamVision() {
        phoneCam = OpenCvCameraFactory.getInstance().createWebcam(
                hardwareMap.get(WebcamName::class.java, "Webcam 1"),
                cameraMonitorViewId
        )
        phoneCam?.openCameraDeviceAsync {
            phoneCam?.openCameraDevice()
            phoneCam?.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
        }

        FtcDashboard.getInstance().startCameraStream(phoneCam!!, 0.0)
    }

    fun initInternalCamVision() {
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera2(
                OpenCvInternalCamera2.CameraDirection.BACK, cameraMonitorViewId
        )
        phoneCam?.openCameraDeviceAsync {
            phoneCam?.openCameraDevice()
            phoneCam?.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
        }

        FtcDashboard.getInstance().startCameraStream(phoneCam!!, 0.0)
    }

    fun initCamVision() {
        try {
            initWebcamVision()
        } catch(ignored: Exception) {
            initInternalCamVision()
        }
    }

    fun initRingVision() {
        ringPipeline = RingPipeline()

        phoneCam?.setPipeline(ringPipeline)
    }

    fun initRingVision2() {
        ringPipeline2 = RingPipeline2()
        phoneCam?.setPipeline(ringPipeline2)
    }

    fun initRectRingVision() {
        rectRingPipeline = RectRingPipeline()
        phoneCam?.setPipeline(rectRingPipeline)
    }

    fun close() {
        phoneCam?.closeCameraDeviceAsync { }
    }

}

enum class RingHeight {
    ZERO, ONE, FOUR
}