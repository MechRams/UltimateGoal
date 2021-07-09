package org.firstinspires.ftc.teamcode.autonomous

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.OpModeType
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterRunCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionMiddleCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionSaveCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawCloseCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawOpenCmd
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.drive.DriveConstants
import org.firstinspires.ftc.teamcode.vision.RingHeight

@Autonomous(name = "Rojo Completo", group = "aaaa", preselectTeleOp = "TeleOp")
class AutonomoRojoCompleto : MechOpMode(OpModeType.AUTO, usingIMU = false, usingRR = true) {

    val d by lazy { SampleMecanumDrive(hardwareMap) }

    val startPose = Pose2d(-64.0,-50.0, Math.toRadians(0.0))

    val secondWobblePoseRight = Pose2d(-36.0, -36.0, Math.toRadians(138.0))
    val secondWobblePoseLeft = Pose2d(-40.0, -17.0, Math.toRadians(200.0))

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
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmPositionMiddleCmd() }
            .lineTo(Vector2d(-10.0, -70.0))
            .addDisplacementMarker { + dropWobble(saveArm = false) }
            .waitSeconds(1.0)

            // go torwards 2nd wobble
            .lineToSplineHeading(secondWobblePoseRight)
            .UNSTABLE_addTemporalMarkerOffset(0.0){ + ArmClawCloseCmd() }
            .waitSeconds(1.0)

            // place 2nd wobble
            .lineToSplineHeading(Pose2d(-10.0, -68.0, Math.toRadians(-15.0)))
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmClawOpenCmd() }
            .waitSeconds(2.0)
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmPositionSaveCmd() }

            // shoot rings to high goal
            .lineToSplineHeading(Pose2d(-15.0, -38.0, Math.toRadians(180.0)))
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + shootRings(0.39) }
            .waitSeconds(6.0)

            // park
            .lineTo(Vector2d(8.0, -38.0))

            .build()

    fun stackB() = d.trajectorySequenceBuilder(startPose)
            // drop first wobble
            .lineTo(Vector2d(-10.0, -70.0))
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmPositionMiddleCmd() }
            .lineTo(Vector2d(17.0, -44.0))
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + dropWobble(saveArm = false) }
            .waitSeconds(1.0)

            // shoot rings to high goal
            .lineToSplineHeading(Pose2d(-15.0, -38.0, Math.toRadians(180.0)))
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + shootRings(0.39) }
            .waitSeconds(6.0)

            // go torwards 2nd wobble
            .strafeTo(Vector2d(-15.0, -15.0))
            .lineToSplineHeading(Pose2d(-40.0, -15.0, Math.toRadians(188.0)))
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmClawCloseCmd() }
            .waitSeconds(1.0)

            // place 2nd wobble
            .lineToSplineHeading(Pose2d(22.0, -44.0, Math.toRadians(5.0)))
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmClawOpenCmd() }
            .waitSeconds(0.5)
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmPositionSaveCmd() }

            .lineTo(Vector2d(8.0, -38.0))

            .build()

    fun stackC() = d.trajectorySequenceBuilder(startPose)
            .lineTo(Vector2d(-10.0, -70.0),
                    SampleMecanumDrive.getVelocityConstraint(40.0, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                    SampleMecanumDrive.getAccelerationConstraint(34.0)
            )
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmPositionMiddleCmd() }
            // drop first wobble
            .lineTo(Vector2d(49.0, -73.0),
                SampleMecanumDrive.getVelocityConstraint(40.0, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                SampleMecanumDrive.getAccelerationConstraint(35.0)
            )
            .addDisplacementMarker {
                + dropWobble(saveArm = false)
                + ShooterRunCmd(0.39)
            }

            // shoot rings to high goal
            .lineToSplineHeading(Pose2d(-15.0, -38.0, Math.toRadians(180.0)))
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + shootRings(0.39, waitShooter = false) }
            .waitSeconds(4.0)

            // go torwards 2nd wobble
            .strafeTo(Vector2d(-15.0, -15.0))
            .lineToSplineHeading(secondWobblePoseLeft)
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmClawCloseCmd() }
            .waitSeconds(0.5)

            // drop 2nd wobble
            .strafeTo(Vector2d(-18.0, -18.0))
            .lineToLinearHeading(Pose2d(49.0, -73.0, 0.0))
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmClawOpenCmd() }
            .UNSTABLE_addTemporalMarkerOffset(0.0) { + ArmPositionSaveCmd() }

            // park
            .lineTo(Vector2d(14.0, -44.0),
                SampleMecanumDrive.getVelocityConstraint(50.0, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                SampleMecanumDrive.getAccelerationConstraint(45.0)
            )

            .build()

}