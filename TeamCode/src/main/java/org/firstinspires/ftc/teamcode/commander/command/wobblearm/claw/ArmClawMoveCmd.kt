package org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw

import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

open class ArmClawMoveCmd(private val clawPosition: Double) : DeltaCommand() {

    val armSubsystem = require<WobbleArmSubsystem>()

    override fun run() {
        armSubsystem.wobbleClawServo.position = clawPosition
    }

}

class ArmClawOpenCmd(armSubsystem: WobbleArmSubsystem) : ArmClawMoveCmd(1.0)

class ArmClawCloseCmd(armSubsystem: WobbleArmSubsystem) : ArmClawMoveCmd(0.0)