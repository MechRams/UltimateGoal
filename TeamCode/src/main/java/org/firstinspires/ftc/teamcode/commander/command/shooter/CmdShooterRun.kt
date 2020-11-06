package org.firstinspires.ftc.teamcode.commander.command.shooter

import com.github.serivesmejia.deltautils.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterSubsystem

open class CmdShooterRun(val shooterSubsystem: ShooterSubsystem, val rpm: Double) : DeltaCommand() {

    init {
        require(shooterSubsystem)
    }

    override fun init() {
        shooterSubsystem.setTargetRPM(rpm);
    }

    override fun run() { }

    override fun end(interrupted: Boolean) { }

}