package org.firstinspires.ftc.teamcode.commander.command.drive

import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.MecanumDriveSubsystem

class DriveStopCmd(private val driveSubsystem: MecanumDriveSubsystem) : DeltaCommand() {

    init {
        require(driveSubsystem)
    }

    override fun run() {
        driveSubsystem.drive.joystickRobotCentric(0.0, 0.0, 0.0, 1.0)
    }

}