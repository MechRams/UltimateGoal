package org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw

import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

open class CmdArmClawMove(private val clawPosition: Double) : DeltaCommand() {

    val armSubsystem = require<WobbleArmSubsystem>()

    override fun run() {
        armSubsystem.wobbleClawServo.position = clawPosition
    }

}

class CmdArmClawOpen(armSubsystem: WobbleArmSubsystem) : CmdArmClawMove(1.0)

class CmdArmClawClose(armSubsystem: WobbleArmSubsystem) : CmdArmClawMove(0.0)