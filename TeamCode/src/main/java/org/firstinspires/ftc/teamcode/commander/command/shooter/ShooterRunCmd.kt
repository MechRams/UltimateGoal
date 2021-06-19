package org.firstinspires.ftc.teamcode.commander.command.shooter

import com.github.serivesmejia.deltacommander.DeltaCommand
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterSubsystem

open class ShooterRunCmd(val powerSupplier: () -> Double, val isVelocity: Boolean) : DeltaCommand() {

    private val shooterSubsystem = require<ShooterSubsystem>()

    constructor(powerSupplier: () -> Double) : this(powerSupplier, false)

    constructor(power: Double) : this({ power })

    override fun init() = shooterSubsystem.run {
        leftMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        rightMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    override fun run() = shooterSubsystem.run {
        if(!isVelocity) {
            leftMotor.power = powerSupplier()
            rightMotor.power = powerSupplier()
        } else {
            leftMotor.velocity = powerSupplier()
            rightMotor.velocity = powerSupplier()
        }
    }

    override fun end(interrupted: Boolean) = shooterSubsystem.run {
        leftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rightMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

}

class ShooterStopCmd : ShooterRunCmd(0.0)