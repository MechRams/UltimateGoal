package org.firstinspires.ftc.teamcode.commander.command.wobblearm

import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

class CmdArmPositionSave(armSubsystem: WobbleArmSubsystem) : CmdArmPositionRun(armSubsystem, armSubsystem.ARM_SAVE_POSITION)