package org.firstinspires.ftc.teamcode.commander.command.wobblearm

import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

class CmdArmPositionMiddle(armSubsystem: WobbleArmSubsystem) : CmdArmPositionRun(armSubsystem, armSubsystem.ARM_MIDDLE_POSITION)