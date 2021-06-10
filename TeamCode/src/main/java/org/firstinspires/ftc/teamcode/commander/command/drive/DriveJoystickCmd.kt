package org.firstinspires.ftc.teamcode.commander.command.drive

import com.github.serivesmejia.deltacommander.DeltaCommand
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.commander.subsystem.MecanumDriveSubsystem

class DriveJoystickCmd(
    private val gamepad: Gamepad
) : DeltaCommand() {

    private val driveSubsystem = require<MecanumDriveSubsystem>()

    override fun run() {
        driveSubsystem.drive.joystickRobotCentric(gamepad, true, 0.7)
    }

}