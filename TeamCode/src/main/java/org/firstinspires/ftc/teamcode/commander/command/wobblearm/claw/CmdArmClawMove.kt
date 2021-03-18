package org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw

import com.github.serivesmejia.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

open class CmdArmClawMove(private val armSubsystem: WobbleArmSubsystem,
                          private val clawPosition: Double) : DeltaCommand() {

    init {
        require(armSubsystem)
    }

    override fun run() {
        armSubsystem.wobbleClawServo.position = clawPosition
    }

}

class CmdArmClawOpen(armSubsystem: WobbleArmSubsystem) : CmdArmClawMove(armSubsystem, 1.0)

class CmdArmClawClose(armSubsystem: WobbleArmSubsystem) : CmdArmClawMove(armSubsystem, 0.0)