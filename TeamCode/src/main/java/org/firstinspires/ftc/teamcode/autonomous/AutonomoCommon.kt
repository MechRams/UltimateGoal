package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltacommander.command.DeltaWaitCmd
import com.github.serivesmejia.deltacommander.command.DeltaWaitConditionCmd
import com.github.serivesmejia.deltacommander.dsl.deltaSequence
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterAutoFlick
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterRunCmd
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterStopCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionMiddleCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionSaveCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawCloseCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawOpenCmd

fun dropWobble() = deltaSequence {
    // move the arm out
    - ArmPositionMiddleCmd()

    // open the claw
    - ArmClawOpenCmd()
    // wait a little for the wobble goal to fall
    - DeltaWaitCmd(1.0)
    // close the claw
    - ArmClawCloseCmd()

    // move the arm back to save (in) position
    - ArmPositionSaveCmd()
}

fun MechOpMode.shootRings() = deltaSequence {
    // start running the shooter (non-blocking)
    - ShooterRunCmd(1.0).dontBlock()

    // wait until the shooter reaches a certain velocity
    - DeltaWaitConditionCmd { shooterSub.avgVelocity >= 30000 }

    // shoot the rings! the servo will move in and out 3 times
    - ShooterAutoFlick(3)
    // stop the shooter after the servo finishes flicking (zero velocity)
    - ShooterStopCmd()
}