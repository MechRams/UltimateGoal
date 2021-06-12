package org.firstinspires.ftc.teamcode.commander.command.intakeconvey

import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.command.shooter.CmdShooterRun
import org.firstinspires.ftc.teamcode.commander.subsystem.IntakeConveySubsystem
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterSubsystem

open class CmdIntakeConveyRun(val power: Double) : DeltaCommand() {

    private val subsystem = require<IntakeConveySubsystem>()

    override fun init() {}

    override fun run() {
        subsystem.intakeConveyMotor.power = power
    }

    override fun end(interrupted: Boolean) {
        subsystem.intakeConveyMotor.power = 0.0
    }

}

class CmdIntakeConveyIn : CmdIntakeConveyRun(1.0)

class CmdIntakeConveyOut : CmdIntakeConveyRun(-1.0)

class CmdIntakeConveyStop : CmdIntakeConveyRun(0.0)