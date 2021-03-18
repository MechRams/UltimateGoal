package org.firstinspires.ftc.teamcode.commander.command.shooter

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterSubsystem

open class CmdShooterRun(val shooterSubsystem: ShooterSubsystem, val powerSupplier: () -> Double) : DeltaCommand() {

    constructor(shooterSubsystem: ShooterSubsystem, power: Double) : this(shooterSubsystem, { power })

    init {
        require(shooterSubsystem)
    }

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

class CmdShooterStop(shooterSubsystem: ShooterSubsystem) : CmdShooterRun(shooterSubsystem, 0.0)