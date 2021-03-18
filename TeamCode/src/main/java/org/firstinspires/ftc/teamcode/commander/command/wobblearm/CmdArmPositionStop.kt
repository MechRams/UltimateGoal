package org.firstinspires.ftc.teamcode.commander.command.wobblearm

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

class CmdArmPositionStop(val armSubsystem: WobbleArmSubsystem) : DeltaCommand() {

    init {
        require(armSubsystem)
    }

    override fun init() {
        armSubsystem.armMoving = false
        armSubsystem.wobbleArmMotor.setRunMode(Motor.RunMode.VelocityControl)
    }

    override fun run() { }

}