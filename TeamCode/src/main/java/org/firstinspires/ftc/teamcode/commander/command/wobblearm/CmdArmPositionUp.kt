package org.firstinspires.ftc.teamcode.commander.command.wobblearm

import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

class CmdArmPositionUp(armSubsystem: WobbleArmSubsystem) : CmdArmPositionRun(armSubsystem, armSubsystem.ARM_UP_POSITION)