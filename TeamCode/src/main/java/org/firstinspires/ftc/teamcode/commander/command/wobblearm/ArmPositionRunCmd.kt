package org.firstinspires.ftc.teamcode.commander.command.wobblearm

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.github.serivesmejia.deltacommander.DeltaCommand
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

class ArmPositionMiddleCmd : ArmPositionRunCmd({ Constants.ARM_MIDDLE_POSITION })

class ArmPositionUpCmd : ArmPositionRunCmd({ Constants.ARM_UP_POSITION })

class ArmPositionSaveCmd: ArmPositionRunCmd({ Constants.ARM_SAVE_POSITION })