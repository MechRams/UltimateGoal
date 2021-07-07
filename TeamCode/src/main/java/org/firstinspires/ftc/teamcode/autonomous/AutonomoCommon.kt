package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltacommander.DeltaCommand
import com.github.serivesmejia.deltacommander.dsl.deltaSequence
import com.github.serivesmejia.deltamath.geometry.Rot2d
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

fun MechOpMode.shootRings(shooterPower: Double = Constants.shooterHighGoalPower) = deltaSequence {
    // start running the shooter (non-blocking)
    - ShooterRunCmd(shooterPower).dontBlock()

    // wait until the shooter reaches a certain velocity
    - waitFor { shooterSub.avgVelocity >= 1000.0 }
    - waitForSeconds(1.0)

    // shoot the rings! the servo will move in and out 3 times
    - ShooterAutoFlick(3)
    // stop the shooter after the servo finishes flicking (zero velocity)
    - ShooterStopCmd().dontBlock()
}

fun MechOpMode.shootRingsPowershot(shooterPower: Double = Constants.shooterHighGoalPower) = deltaSequence {
    // start running the shooter (non-blocking)
    - ShooterRunCmd(shooterPower).dontBlock()

    // wait until the shooter reaches a certain velocity
    - waitFor { shooterSub.avgVelocity >= 900.0 }
    - waitForSeconds(1.0)

    // shoot the rings! the servo will move in and out 3 times
    - ShooterAutoFlick(1)
    - drive.rotate(Rot2d.degrees(6.0), 0.3)

    - ShooterAutoFlick(1)
    - drive.rotate(Rot2d.degrees(6.0), 0.3)

    - ShooterAutoFlick(1)
    - drive.rotate(Rot2d.degrees(6.0), 0.3)

    // stop the shooter after the servo finishes flicking (zero velocity)
    - ShooterStopCmd().dontBlock()
}

fun grabWobble() = deltaSequence {
    // close the claw once the wobble goal is in
    - ArmClawCloseCmd().dontBlock()
    // wait a little for the claw to close
    - waitForSeconds(0.8)
    // move the arm up
    - ArmPositionSaveCmd().dontBlock()
}

fun wobbleMiddleOpen() = deltaSequence {
    //move the arm to the middle
    - ArmPositionMiddleCmd().dontBlock()
    // open the claw at the same time
    - ArmClawOpenCmd().dontBlock()
}