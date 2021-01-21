package org.firstinspires.ftc.teamcode.commander.command.intakeconvey

import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.IntakeConveySubsystem

open class CmdIntakeConveyRun(val subsystem: IntakeConveySubsystem, val power: Double) : DeltaCommand() {

    init {
        require(subsystem)
    }

    override fun init() {}

    override fun run() {
        subsystem.intakeConveyMotor.set(power)
    }

    override fun end(interrupted: Boolean) {
        subsystem.intakeConveyMotor.set(0.0)
    }

}