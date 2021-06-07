package org.firstinspires.ftc.teamcode.commander.subsystem

import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.github.serivesmejia.deltadrive.drivebase.MecanumDrive
import com.github.serivesmejia.deltadrive.hardware.DeltaHardwareHolonomic
import org.firstinspires.ftc.teamcode.commander.command.drive.DriveStopCmd

class MecanumDriveSubsystem(deltaHdw: DeltaHardwareHolonomic) : DeltaSubsystem() {

    val drive = MecanumDrive(deltaHdw)

    override fun loop() {
        setDefaultCommand(DriveStopCmd(this))
    }

}