package org.firstinspires.ftc.teamcode.commander.command.wobblearm

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

class ArmPositionStopCmd : DeltaCommand() {

    val armSubsystem = require<WobbleArmSubsystem>()

    override fun init() {
        armSubsystem.armMoving = false
        armSubsystem.wobbleArmMotor.setRunMode(Motor.RunMode.VelocityControl)
    }

    override fun run() { }

}