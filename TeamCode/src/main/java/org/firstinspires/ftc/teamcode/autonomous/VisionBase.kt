package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.vision.RingPipeline
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera

open class VisionBase : LinearOpMode() {

    lateinit var phoneCam: OpenCvCamera

    val ringPipeline = RingPipeline()

    override fun runOpMode() { }

    fun initRingVision() {

        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId)

        phoneCam.setPipeline(ringPipeline)

        phoneCam.openCameraDeviceAsync { phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT) }

        telemetry.addData("[>]", "Ready")
        telemetry.update()

    }

}