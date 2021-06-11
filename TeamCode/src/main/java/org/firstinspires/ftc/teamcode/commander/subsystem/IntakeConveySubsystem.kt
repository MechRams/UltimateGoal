package org.firstinspires.ftc.teamcode.commander.subsystem

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.github.serivesmejia.deltacommander.DeltaSubsystem
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.CmdIntakeConveyStop

class IntakeConveySubsystem(val intakeConveyMotor: MotorEx) : DeltaSubsystem() {
    val isStopped get() = intakeConveyMotor.get() == 0.0
    val isForward get() = intakeConveyMotor.get() > 0.0

    init {
        defaultCommand = CmdIntakeConveyStop()
    }

    override fun loop() {
        intakeConveyMotor.setRunMode(Motor.RunMode.RawPower)
    }
}