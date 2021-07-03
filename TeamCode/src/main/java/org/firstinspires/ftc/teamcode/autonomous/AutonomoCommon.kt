package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltacommander.DeltaCommand
import com.github.serivesmejia.deltacommander.command.DeltaWaitConditionCmd
import com.github.serivesmejia.deltacommander.dsl.deltaSequence
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterAutoFlick
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterRunCmd
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterStopCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionMiddleCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionSaveCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionUpCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawCloseCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawOpenCmd

fun dropWobble() = deltaSequence {
    // open the claw
    - ArmClawOpenCmd().dontBlock()
    // wait a little for the wobble goal to fall
    - waitForSeconds(0.4)
    // close the claw
    - ArmClawCloseCmd().dontBlock()

    // move the arm back to save (in) position
    - ArmPositionSaveCmd().dontBlock()
}

fun MechOpMode.shootRings() = deltaSequence {
    // start running the shooter (non-blocking)
    - ShooterRunCmd(Constants.shooterPower).dontBlock()

    // wait until the shooter reaches a certain velocity
    // TODO test avg velocity to put correct thing here and use this instead of waitForSeconds
    //- waitFor { shooterSub.avgVelocity >= 30000 }
    - waitForSeconds(3.0)

    // shoot the rings! the servo will move in and out 3 times
    - ShooterAutoFlick(3)
    // stop the shooter after the servo finishes flicking (zero velocity)
    - ShooterStopCmd().dontBlock()
}

fun grabWobble(waitForToGrab: DeltaCommand) = deltaSequence {
    - ArmPositionMiddleCmd().dontBlock()
    - ArmClawOpenCmd().dontBlock()

    waitForToGrab.waitFor()

    - ArmClawCloseCmd().dontBlock()
    - waitForSeconds(0.8)
    - ArmPositionUpCmd().dontBlock()
}