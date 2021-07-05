package org.firstinspires.ftc.teamcode.autonomous.test

import com.github.serivesmejia.deltacommander.command.DeltaSequentialCmd
import com.github.serivesmejia.deltacommander.command.DeltaWaitCmd
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltacommander.dsl.deltaSequence
import com.github.serivesmejia.deltamath.geometry.Rot2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.OpModeType
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.IntakeConveyInCmd
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.IntakeConveyStopCmd

@Autonomous(name = "Drive Forward", group = "test", preselectTeleOp = "TeleOp")
class AutonomoDriveForward : MechOpMode(OpModeType.AUTO) {

    override fun run() {
        waitForStart()

        + autoA()

        while(opModeIsActive()) {
            deltaScheduler.update()

            telemetry.addData("fl", hdw.wheelFrontLeft.power)
            telemetry.addData("fr", hdw.wheelFrontRight.power)
            telemetry.addData("bl", hdw.wheelBackLeft.power)
            telemetry.addData("br", hdw.wheelBackRight.power)

            telemetry.addData("fl pos", hdw.wheelFrontLeft.targetPosition)
            telemetry.addData("fr pos", hdw.wheelFrontRight.targetPosition)
            telemetry.addData("bl pos", hdw.wheelBackLeft.targetPosition)
            telemetry.addData("br pos", hdw.wheelBackRight.targetPosition)
            telemetry.update()
        }
    }

    fun autoA() = deltaSequence {
        - drive.encoderForward(50.0, 0.2).markers {
            distanceMarker(20.0) {
                + IntakeConveyInCmd()
            }
        }

        - drive.rotate(Rot2d.degrees(90.0), 0.5).markers {
            rotationMarker(Rot2d.degrees(30.0)) {
                + IntakeConveyStopCmd()
            }
        }

        - drive.encoderStrafeLeft(20.0, 0.3)

        - drive.rotate(Rot2d.degrees(-180.0), 0.5)
    }

}