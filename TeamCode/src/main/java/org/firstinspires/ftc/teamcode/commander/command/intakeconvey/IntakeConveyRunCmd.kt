package org.firstinspires.ftc.teamcode.commander.command.intakeconvey

import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.IntakeConveySubsystem

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

class IntakeConveyInCmd : CmdIntakeConveyRun(1.0)

class IntakeConveyOutCmd : CmdIntakeConveyRun(-1.0)

class IntakeConveyStopCmd : CmdIntakeConveyRun(0.0)