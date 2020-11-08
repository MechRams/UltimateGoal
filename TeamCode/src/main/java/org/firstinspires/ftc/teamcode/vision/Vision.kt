package org.firstinspires.ftc.teamcode.vision

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.vision.RingPipeline

import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera

open class Vision(private val hardwareMap: HardwareMap) {

    var phoneCam: OpenCvCamera? = null;

    var ringPipeline: RingPipeline? = null;
    var ringPipeline2: RingPipeline2? = null;

    private fun initVision() {
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId)
    }

    fun initRingVision() {

        initVision();
        ringPipeline = RingPipeline()

        phoneCam?.setPipeline(ringPipeline)
        phoneCam?.openCameraDeviceAsync { phoneCam?.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT) }

    }

    fun initRingVision2() {

        initVision()
        ringPipeline2 = RingPipeline2()

        phoneCam?.setPipeline(ringPipeline2)
        phoneCam?.openCameraDeviceAsync { phoneCam?.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT) }

    }

}