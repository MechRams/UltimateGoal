package org.firstinspires.ftc.teamcode.commander.command.shooter

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.github.serivesmejia.deltacommander.DeltaCommand
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterSubsystem

open class ShooterRunCmd(val velocitySupplier: () -> Double) : DeltaCommand() {

    private val shooterSubsystem = require<ShooterSubsystem>()

    constructor(power: Double) : this({ power })

    override fun init() {
        shooterSubsystem.shooterMotors.setRunMode(Motor.RunMode.RawPower)
        shooterSubsystem.leftMotor.motorEx.mode = DcMotor.RunMode.RUN_USING_ENCODER
        shooterSubsystem.rightMotor.motorEx.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    override fun run() {
        shooterSubsystem.leftMotor.motorEx.velocity = velocitySupplier()
        shooterSubsystem.rightMotor.motorEx.velocity = velocitySupplier()
    }

    override fun end(interrupted: Boolean) {
        shooterSubsystem.shooterMotors.setRunMode(Motor.RunMode.RawPower)
        shooterSubsystem.leftMotor.motorEx.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        shooterSubsystem.rightMotor.motorEx.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

}

class ShooterStopCmd : ShooterRunCmd(0.0)