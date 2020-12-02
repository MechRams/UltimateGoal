package org.firstinspires.ftc.teamcode.commander.command.wobblearm

import com.github.serivesmejia.deltautils.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

open class CmdArmPositionRun(private val armSubsystem: WobbleArmSubsystem, private val position: Double) : DeltaCommand() {

    init {
        require(armSubsystem)
    }

    override fun init() {

        armSubsystem.armController.reset()

        armSubsystem.armController.setSetpoint(position)
                                  .setDeadzone(0.1)
                                  .setErrorTolerance(10.0)
                                  .setInitialPower(0.6)

        armSubsystem.armMoving = true

    }

    override fun run() { }

    override fun end(interrupted: Boolean) {
        armSubsystem.armController.reset()
        armSubsystem.armMoving = false
    }

}