package org.firstinspires.ftc.teamcode.commander.subsystem

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.github.serivesmejia.deltacommander.DeltaSubsystem
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.CmdIntakeConveyStop

class IntakeConveySubsystem(val intakeConveyMotor: MotorEx) : DeltaSubsystem() {
    init {
        defaultCommand = CmdIntakeConveyStop(this)
    }

    override fun loop() {
        intakeConveyMotor.setRunMode(Motor.RunMode.RawPower)
    }
}