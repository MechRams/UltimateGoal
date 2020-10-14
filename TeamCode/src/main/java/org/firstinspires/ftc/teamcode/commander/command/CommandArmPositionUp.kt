package org.firstinspires.ftc.teamcode.commander.command

import com.github.serivesmejia.deltautils.deltacommander.DeltaCommand
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem

class CommandArmPositionUp(val armSubsystem: WobbleArmSubsystem) : DeltaCommand() {

    init {
        require(armSubsystem)
    }

    override fun init() {

        armSubsystem.armController.reset()

        armSubsystem.armController.setSetpoint(armSubsystem.ARM_MIDDLE_POSITION)
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