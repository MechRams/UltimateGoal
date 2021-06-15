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
        armSubsystem.wobbleArmMotor.setPositionTolerance(20.0)

        armSubsystem.armMoving = true
    }

    override fun run() {
        armSubsystem.wobbleArmMotor.setTargetPosition(positionSupplier())
    }

    override fun end(interrupted: Boolean) {
        armSubsystem.wobbleArmMotor.setRunMode(Motor.RunMode.VelocityControl)
        armSubsystem.armMoving = false
    }

}

class ArmPositionMiddleCmd : ArmPositionRunCmd({ Constants.armMiddlePosition })

class ArmPositionUpCmd : ArmPositionRunCmd({ Constants.armUpPosition })

class ArmPositionSaveCmd : ArmPositionRunCmd({ Constants.armSavePosition })

class ArmPositionResetCmd : DeltaCommand() {

    val armSubsystem = require<WobbleArmSubsystem>()

    private val timer = ElapsedTime()

    override fun init() {
        timer.reset()
    }

    override fun run() {
        armSubsystem.wobbleArmMotor.set(-0.4)
        if(timer.seconds() >= 5) {
            armSubsystem.wobbleArmMotor.resetEncoder()
            finish()
        }
    }

    override fun end(interrupted: Boolean) {
        armSubsystem.wobbleArmMotor.set(0.5)
    }

}