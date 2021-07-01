package org.firstinspires.ftc.teamcode.commander.command.shooter

import com.github.serivesmejia.deltacommander.DeltaCommand
import com.noahbres.jotai.StateMachine
import com.noahbres.jotai.StateMachineBuilder
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterFlickerSubsystem

class ShooterFlickCmd : DeltaCommand() {

    val shooterSub = require<ShooterFlickerSubsystem>()

    val timer = ElapsedTime()

    override fun init() {
        timer.reset()
    }

    override fun run() {
        shooterSub.flickerServo.position = Constants.flickerInPos
    }

    //override fun endCondition() = timer.seconds() >= 0.5

}

class ShooterFlickOutCmd : DeltaCommand() {

    val shooterSub = require<ShooterFlickerSubsystem>()

    override fun run() {
        shooterSub.flickerServo.position = Constants.flickerOutPos
    }

}