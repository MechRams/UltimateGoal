package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltacommander.command.DeltaSequentialCmd
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltacommander.dsl.deltaSequence
import com.github.serivesmejia.deltamath.geometry.Rot2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.IntakeConveyInCmd

@Autonomous(name = "Drive Forward", group = "test", preselectTeleOp = "TeleOp")
class AutonomoDriveForward : MechOpMode() {

    override fun run() {
        waitForStart()

        + autoA()

        deltaScheduler.updateUntilNoCommands()
    }

    fun autoA() = deltaSequence {
        drive.timeForward(0.2, 4.0).markers {
            timeMarker(2.0) {
                + IntakeConveyInCmd().stopAfter(3.0)
            }
        }.command()

        drive.rotate(Rot2d.degrees(90.0), 0.5).command()
    }

}