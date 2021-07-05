package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltacommander.dsl.deltaSequence
import com.github.serivesmejia.deltamath.geometry.Rot2d
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.OpModeType
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionMiddleCmd
import org.firstinspires.ftc.teamcode.vision.RingPipeline2

@Autonomous(name = "Rojo Completo", group = "final", preselectTeleOp = "TeleOp")
class AutonomoRojoCompleto : MechOpMode(OpModeType.AUTO) {

    override fun run() {
        deltaHdw.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        //vision.initCamVision()
        //vision.initRingVision2()

        waitForStart()

        when(vision.ringPipeline2?.getLatestMostLikelyHeight() ?: RingPipeline2.RingHeight.ZERO) {
            RingPipeline2.RingHeight.ZERO -> stackA()
            RingPipeline2.RingHeight.ONE  -> stackB()
            RingPipeline2.RingHeight.FOUR -> stackC()
        }.schedule()

        deltaScheduler.updateUntilNoCommands {
            deltaHdw.clearBulkCache()

            telemetry.addData("shooter avg velocity", shooterSub.avgVelocity)
            telemetry.update()
        }
    }

    fun stackA() = deltaSequence {

        /* DROPPING THE FIRST WOBBLE GOAL */

        - drive.encoderTiltForwardRight(47.0, 1.0, 4.0)
        - ArmPositionMiddleCmd().dontBlock()

        - drive.encoderForward(25.0, 1.0, 4.0)
        - dropWobble()

        /* SHOOTING RINGS */

        // rotate torwards the high goal
        - drive.rotate(Rot2d.degrees(200.0), 0.7)

        // shoot the 3 rings
        - shootRings()

        /* GRABBING THE SECOND WOBBLE GOAL */

        // rotate torwards the 2nd wobble goal
        - drive.rotate(Rot2d.degrees(-40.0), 0.7)

        val forwardWobbleCmd = drive.encoderForward(30.0, 0.5, 4.0).command
        val grabWobbleCmd = grabWobble(forwardWobbleCmd)

        // start moving the arm to the out position
        - grabWobbleCmd.dontBlock()
        // drive torwards the 2nd wobble goal hopefully
        - forwardWobbleCmd
        // wait for the wobble goal to be grabbed
        - grabWobbleCmd.waitFor()

        /* DROPPING THE SECOND WOBBLE GOAL */

        // rotate torwards the A square
        - drive.rotate(Rot2d.degrees(90.0), 0.6)

        // start moving the arm to middle
        - ArmPositionMiddleCmd().dontBlock()

        // move to the A square
        - drive.encoderForward(30.0, 1.0)
        - dropWobble()
    }

    fun stackB() = deltaSequence {
        /* DROPPING THE FIRST WOBBLE GOAL */
        - drive.encoderForward(25.0, 1.0)

        - ArmPositionMiddleCmd().dontBlock()

        // move to the B square
        - drive.encoderTiltForwardLeft(47.0, 1.0, 4.0)
        - dropWobble()

        /* SHOOTING RINGS */

        // rotate torwards the high goal
        - drive.rotate(Rot2d.degrees(-180.0), 0.7)

        // shoot the 3 rings
        - shootRings()

        /* GRABBING THE SECOND WOBBLE GOAL */

        - drive.rotate(Rot2d.degrees(-20.0), 0.7)

        val forwardWobbleCmd = drive.encoderForward(20.0, 0.5, 4.0).command
        val grabWobbleCmd = grabWobble(forwardWobbleCmd)

        // start moving the arm to the out position
        - grabWobbleCmd.dontBlock()
        // drive torwards the 2nd wobble goal hopefully
        - forwardWobbleCmd
        // wait for the wobble goal to be grabbed
        - grabWobbleCmd.waitFor()

        // move torwards the B square
        - drive.rotate(Rot2d.degrees(200.0), 0.7)
        - drive.encoderTiltForwardRight(10.0, 1.0, 4.0)
        - drive.encoderForward(15.0, 1.0)

        - dropWobble()
    }

    fun stackC() = deltaSequence {
        /* DROPPING THE FIRST WOBBLE GOAL */

        - drive.encoderTiltForwardRight(47.0, 1.0, 4.0)
        - ArmPositionMiddleCmd().dontBlock()

        - drive.encoderForward(45.0, 1.0, 4.0)
        - dropWobble()

        - shootRings()
    }

}