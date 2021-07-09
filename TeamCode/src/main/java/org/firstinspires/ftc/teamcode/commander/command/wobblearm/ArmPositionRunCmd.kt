package org.firstinspires.ftc.teamcode.commander.command.wobblearm

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.github.serivesmejia.deltacommander.DeltaCommand
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

open class ArmPositionRunCmd(private val positionSupplier: () -> Int) : DeltaCommand() {

    val armSubsystem = require<WobbleArmSubsystem>()

    override fun init() {
        armSubsystem.wobbleArmMotor.setRunMode(Motor.RunMode.PositionControl)
        armSubsystem.wobbleArmMotor.setPositionTolerance(10.0)

        armSubsystem.armMoving = true
    }

    override fun run() {
        val pos = positionSupplier()

        armSubsystem.wobbleArmMotor.setTargetPosition(pos)
    }

    override fun end(interrupted: Boolean) {
        armSubsystem.wobbleArmMotor.setRunMode(Motor.RunMode.VelocityControl)
        armSubsystem.armMoving = false
    }

}

class ArmPositionMiddleCmd : ArmPositionRunCmd({ Constants.armMiddlePosition })

class ArmPositionUpCmd : ArmPositionRunCmd({ Constants.armUpPosition })

class ArmPositionSaveCmd : ArmPositionRunCmd({ Constants.armSavePosition })
/*
class ArmPositionResetCmd : DeltaCommand() {

    val armSubsystem = require<WobbleArmSubsystem>()

    override fun run() {
        if(armSubsystem.wobbleTouchSensor.isPressed) {
            armSubsystem.wobbleArmMotor.resetEncoder()
            finish()
            return
        }

        armSubsystem.wobbleArmMotor.set(0.4)
    }

    override fun end(interrupted: Boolean) {
        armSubsystem.wobbleArmMotor.set(0.0)
    }

}*/