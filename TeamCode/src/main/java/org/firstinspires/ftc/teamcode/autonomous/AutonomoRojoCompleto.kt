package org.firstinspires.ftc.teamcode.autonomous

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltacommander.dsl.deltaSequence
import com.github.serivesmejia.deltamath.geometry.Rot2d
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.OpModeType
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionMiddleCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionSaveCmd
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.vision.RingHeight
import org.firstinspires.ftc.teamcode.vision.RingPipeline2

@Autonomous(name = "Rojo Completo", group = "final", preselectTeleOp = "TeleOp")
class AutonomoRojoCompleto : MechOpMode(OpModeType.AUTO, usingIMU = false, usingRR = true) {

    val d by lazy { SampleMecanumDrive(hardwareMap) }

    val startPose = Pose2d(-64.0,-50.0, Math.toRadians(0.0))

    override fun run() {
        vision.initCamVision()
        vision.initRectRingVision()

        while(!isStopRequested && !isStarted) {
            telemetry.addData("[Stack Height]", vision.rectRingPipeline?.detectedHeight ?: RingHeight.ZERO)
            telemetry.update()
        }

        vision.close()

        d.followTrajectorySequenceAsync(when(vision.rectRingPipeline?.detectedHeight ?: RingHeight.ZERO) {
            RingHeight.ZERO -> stackA()
            RingHeight.ONE  -> stackB()
            RingHeight.FOUR -> stackC()
        })

        d.poseEstimate = startPose

        while(opModeIsActive()) {
            deltaScheduler.update()
            d.update()
        }
    }

    fun stackA() = d.trajectorySequenceBuilder(startPose)
            // drop first wobble
            //.addTemporalMarker { + ArmPositionMiddleCmd() }
            .lineTo(Vector2d(-10.0, -70.0))

            //.addDisplacementMarker { + dropWobble() }
            //.waitSeconds(1.0)

            // go torwards 2nd wobble
            .lineToSplineHeading(Pose2d(-36.0, -36.0, Math.toRadians(130.0)))
            //.addDisplacementMarker { + grabWobble() }
            //.waitSeconds(2.0)
            //.UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmPositionMiddleCmd() }

            // place 2nd wobble
            .lineToSplineHeading(Pose2d(-10.0, -68.0, Math.toRadians(5.0)))
            //.addDisplacementMarker { + dropWobble() }
            //.waitSeconds(1.0)
            //.UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmPositionSaveCmd() }

            // shoot rings to high goal
            .lineToSplineHeading(Pose2d(-15.0, -38.0, Math.toRadians(180.0)))

            // park
            .lineTo(Vector2d(8.0, -38.0))

            .build()

    fun stackB() = d.trajectorySequenceBuilder(startPose)
            // drop first wobble
            .lineTo(Vector2d(-10.0, -70.0))
            .lineTo(Vector2d(24.0, -35.0))

            // shoot rings to high goal
            .lineToSplineHeading(Pose2d(-15.0, -38.0, Math.toRadians(180.0)))

            // go torwards 2nd wobble
            .lineToSplineHeading(Pose2d(-48.0, -40.0, Math.toRadians(105.0)))

            // place 2nd wobble
            .lineToSplineHeading(Pose2d(28.0, -36.0, Math.toRadians(5.0)))

            // park
            .lineTo(Vector2d(8.0, -38.0))

            .build()

    fun stackC() = d.trajectorySequenceBuilder(startPose)
            // drop first wobble
            .lineTo(Vector2d(49.0, -70.0))

            // shoot rings to high goal
            .lineToSplineHeading(Pose2d(-15.0, -38.0, Math.toRadians(180.0)))

            // go torwards 2nd wobble
            .lineToSplineHeading(Pose2d(-48.0, -40.0, Math.toRadians(105.0)))

            // drop 2nd wobble
            .lineTo(Vector2d(49.0, -70.0))

            // park
            .lineTo(Vector2d(5.0, -38.0))

            .build()

}