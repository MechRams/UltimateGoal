package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltacommander.command.DeltaSequentialCommand
import com.github.serivesmejia.deltamath.geometry.Rot2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.commander.command.shooter.CmdShooterRun

@Autonomous(name = "Drive Forward", group = "test", preselectTeleOp = "TeleOp")
class AutonomoDriveForward : MechOpMode() {

    override fun run() {
        waitForStart()

        + DeltaSequentialCommand(
            drive.timeForward(0.8, 4.0).timeMarker(2.0) {
                + CmdShooterRun(1.0).stopAfter(3.0)
            }.command,

            drive.rotate(Rot2d.degrees(90.0), 0.5).command
        )
    }

}