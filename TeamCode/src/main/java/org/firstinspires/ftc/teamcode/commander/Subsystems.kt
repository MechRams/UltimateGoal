package org.firstinspires.ftc.teamcode.commander

import com.github.serivesmejia.deltautils.deltacommander.DeltaScheduler
import org.firstinspires.ftc.teamcode.commander.subsystem.IntakeConveySubsystem
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterSubsystem
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem
import org.firstinspires.ftc.teamcode.hardware.Hardware

class Subsystems {
    lateinit var intakeConvey: IntakeConveySubsystem
    lateinit var wobbleArm: WobbleArmSubsystem
    lateinit var shooter: ShooterSubsystem

    fun init(hdw: Hardware) {
        DeltaScheduler.reset()

        intakeConvey = IntakeConveySubsystem(hdw.motorIntakeConvey)
        DeltaScheduler.instance.addSubsystem(intakeConvey)

        wobbleArm = WobbleArmSubsystem(hdw.motorWobbleArm, hdw.servoWobbleClaw)
        DeltaScheduler.instance.addSubsystem(wobbleArm)

        shooter = ShooterSubsystem(hdw.motorShooterLeft, hdw.motorShooterRight)
        DeltaScheduler.instance.addSubsystem(shooter)
    }
}