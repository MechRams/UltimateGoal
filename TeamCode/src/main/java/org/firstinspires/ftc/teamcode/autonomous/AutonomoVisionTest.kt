package org.firstinspires.ftc.teamcode.autonomous

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.vision.Vision

@Autonomous(name="Vision Test", group="test")
class AutonomoVisionTest : LinearOpMode() {

    private lateinit var vision: Vision

    override fun runOpMode() {

        telemetry = FtcDashboard.getInstance().telemetry

        vision = Vision(hardwareMap)
        vision.initRingVision2()

        waitForStart()

        while(opModeIsActive()) {

            telemetry.addData("Ring Stack Pattern", vision.ringPipeline2?.getLatestMostLikelyHeight())

            telemetry.addData("Frame Count", vision.phoneCam?.frameCount)
            telemetry.addData("FPS", "%.2f", vision.phoneCam?.fps)
            telemetry.addData("Total frame time ms", vision.phoneCam?.totalFrameTimeMs)
            telemetry.addData("Pipeline time ms", vision.phoneCam?.pipelineTimeMs)
            telemetry.addData("Overhead time ms", vision.phoneCam?.overheadTimeMs)
            telemetry.addData("Theoretical max FPS", vision.phoneCam?.currentPipelineMaxFps)

            telemetry.update()

        }

        vision.ringPipeline2?.destroy()

    }

}