package org.firstinspires.ftc.teamcode.commander.command.shooter

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterSubsystem

open class ShooterRunCmd(val powerSupplier: () -> Double) : DeltaCommand() {

    private val shooterSubsystem = require<ShooterSubsystem>()

    constructor(power: Double) : this({ power })

    override fun init() {
        shooterSubsystem.shooterMotors.setRunMode(Motor.RunMode.VelocityControl)
    }

    override fun run() {
        shooterSubsystem.shooterMotors.set(powerSupplier())
    }

    override fun end(interrupted: Boolean) {
        shooterSubsystem.shooterMotors.setRunMode(Motor.RunMode.RawPower)
    }

}

class ShooterStopCmd : ShooterRunCmd(0.0)