package org.firstinspires.ftc.teamcode.commander.command.wobblearm

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

open class ArmPositionRunCmd(private val position: Int) : DeltaCommand() {

    val armSubsystem = require<WobbleArmSubsystem>()

    override fun init() {
        armSubsystem.wobbleArmMotor.setRunMode(Motor.RunMode.PositionControl)
        armSubsystem.wobbleArmMotor.setTargetPosition(position)
        armSubsystem.wobbleArmMotor.setPositionTolerance(20.0)

        armSubsystem.armMoving = true
    }

    override fun run() { }

    override fun end(interrupted: Boolean) {
        armSubsystem.wobbleArmMotor.setRunMode(Motor.RunMode.VelocityControl)
        armSubsystem.armMoving = false
    }

}

class ArmPositionMiddleCmd : ArmPositionRunCmd(WobbleArmSubsystem.ARM_MIDDLE_POSITION)

class ArmPositionUpCmd : ArmPositionRunCmd(WobbleArmSubsystem.ARM_UP_POSITION)

class ArmPositionSaveCmd: ArmPositionRunCmd(WobbleArmSubsystem.ARM_SAVE_POSITION)