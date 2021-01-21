package org.firstinspires.ftc.teamcode.commander.command.shooter

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterSubsystem

open class CmdShooterRun(val shooterSubsystem: ShooterSubsystem, val power: Double) : DeltaCommand() {

    init {
        require(shooterSubsystem)
    }

    override fun init() {
        shooterSubsystem.shooterMotors.setRunMode(Motor.RunMode.VelocityControl)
    }

    override fun run() {
        shooterSubsystem.shooterMotors.set(power)
    }

    override fun end(interrupted: Boolean) {
        shooterSubsystem.shooterMotors.setRunMode(Motor.RunMode.RawPower)
    }

}