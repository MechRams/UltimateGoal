package org.firstinspires.ftc.teamcode.teleop

import com.github.serivesmejia.deltacommander.DeltaScheduler
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.commander.command.drive.DriveJoystickCmd

class MechDriveTeleOp : MechOpMode(true) {

    override fun run() {
        telemetry.addData("[>]", "Listos!")
        telemetry.update()

        DeltaScheduler.instance.schedule(DriveJoystickCmd(subsystems.drive, gamepad1))

        waitForStart()

        while(opModeIsActive()) {
            telemetry.addData("[>]", "Nyooom")
            telemetry.update()

            DeltaScheduler.instance.update()
        }
    }

}