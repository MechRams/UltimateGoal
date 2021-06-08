package org.firstinspires.ftc.teamcode.commander

import com.github.serivesmejia.deltacommander.DeltaScheduler
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltadrive.hardware.DeltaHardwareHolonomic
import org.firstinspires.ftc.teamcode.commander.subsystem.IntakeConveySubsystem
import org.firstinspires.ftc.teamcode.commander.subsystem.MecanumDriveSubsystem
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterSubsystem
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem
import org.firstinspires.ftc.teamcode.hardware.Hardware

class Subsystems {
    lateinit var intakeConvey: IntakeConveySubsystem
    lateinit var wobbleArm: WobbleArmSubsystem
    lateinit var shooter: ShooterSubsystem
    lateinit var drive: MecanumDriveSubsystem

    fun init(hdw: Hardware, deltaHdw: DeltaHardwareHolonomic, onlyChassis: Boolean) {
        drive = MecanumDriveSubsystem(deltaHdw)

        if(!onlyChassis) {
            intakeConvey = IntakeConveySubsystem(hdw.motorIntakeConvey)
            deltaScheduler.addSubsystem(intakeConvey)

            wobbleArm = WobbleArmSubsystem(hdw.motorWobbleArm, hdw.servoWobbleClaw)
            deltaScheduler.addSubsystem(wobbleArm)

            shooter = ShooterSubsystem(hdw.motorShooterLeft, hdw.motorShooterRight)
            deltaScheduler.addSubsystem(shooter)
        }
    }
}