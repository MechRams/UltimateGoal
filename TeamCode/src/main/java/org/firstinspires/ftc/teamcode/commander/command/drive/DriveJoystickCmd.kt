package org.firstinspires.ftc.teamcode.commander.command.drive

import com.github.serivesmejia.deltacommander.DeltaCommand
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.commander.subsystem.MecanumDriveSubsystem

class DriveJoystickCmd(
    private val driveSubsystem: MecanumDriveSubsystem,
    private val gamepad: Gamepad
) : DeltaCommand() {

    init {
        require(driveSubsystem)
    }

    override fun run() {
        driveSubsystem.drive.joystickRobotCentric(gamepad, true, 0.7)
    }

}