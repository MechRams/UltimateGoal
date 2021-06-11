package org.firstinspires.ftc.teamcode.teleop

import com.github.serivesmejia.deltacommander.DeltaScheduler
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.commander.command.drive.DriveJoystickCmd
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="Drive Test TeleOp", group="Test")
@Disabled
class MechDriveTeleOp : MechOpMode(true, false) {

    override fun run() {
        telemetry.addData("[>]", "Listos!")
        telemetry.update()

        waitForStart()

        DriveJoystickCmd(gamepad1).schedule()

        while(opModeIsActive()) {
            telemetry.addData("[>]", "Nyooom")
            telemetry.update()

            deltaScheduler.update()
        }
    }

}