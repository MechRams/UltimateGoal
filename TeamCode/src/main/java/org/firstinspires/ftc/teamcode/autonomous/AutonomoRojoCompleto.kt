package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltacommander.dsl.deltaSequence
import com.github.serivesmejia.deltamath.geometry.Rot2d
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.OpModeType
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionMiddleCmd
import org.firstinspires.ftc.teamcode.vision.RingHeight
import org.firstinspires.ftc.teamcode.vision.RingPipeline2

@Autonomous(name = "Rojo Completo", group = "final", preselectTeleOp = "TeleOp")
class AutonomoRojoCompleto : MechOpMode(OpModeType.AUTO) {

    override fun run() {
        deltaHdw.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        vision.initCamVision()
        vision.initRectRingVision()

        while(!isStopRequested && !isStarted) {
            telemetry.addData("[Stack Height]", vision.rectRingPipeline?.detectedHeight ?: RingHeight.ZERO)
            telemetry.update()
        }

        vision.close()

        when(vision.rectRingPipeline?.detectedHeight ?: RingHeight.ZERO) {
            RingHeight.ZERO -> stackA()
            RingHeight.ONE  -> stackB()
            RingHeight.FOUR -> stackC()
        }.schedule()

        deltaScheduler.updateUntilNoCommands {
            deltaHdw.clearBulkCache()
        }
    }

    fun stackA() = deltaSequence {
        /* DROPPING THE FIRST WOBBLE GOAL */

        - drive.encoderTiltForwardRight(50.0, 0.6)
        - ArmPositionMiddleCmd().dontBlock()

        - drive.encoderForward(30.0, 0.8)
        - dropWobble()

        /* SHOOTING RINGS */
        // rotate torwards the high goal
        - drive.encoderTiltBackwardsLeft(24.0, 0.3)
        //- drive.rotate(Rot2d.degrees(183.0), 0.6) // high goal
        - drive.rotate(Rot2d.degrees(195.0), 0.6)

        // shoot the 3 rings
        //- shootRings(0.39) // high goal
        - shootRingsPowershot(0.37)

        /* GRABBING THE SECOND WOBBLE GOAL */

        // rotate torwards the 2nd wobble goal
        - drive.rotate(Rot2d.degrees(-90.0), 0.3)

        - wobbleMiddleOpen().dontBlock()

        // drive torwards the 2nd wobble goal hopefully
        - drive.encoderForward(64.0, 0.3)
        // wait for the wobble goal to be grabbed
        - grabWobble()

        - drive.rotate(Rot2d.degrees(175.0), 0.6)

        - ArmPositionMiddleCmd().dontBlock()

        - drive.encoderForward(88.0, 0.45)

        - dropWobble()

        - drive.encoderBackwards(15.0, 0.6)
        - drive.rotate(Rot2d.degrees(50.0), 0.6)
        - drive.encoderForward(35.0, 0.7)
    }

    fun stackB() = deltaSequence {
        /* DROPPING THE FIRST WOBBLE GOAL */
        - drive.encoderTiltForwardRight(10.0, 0.3)

        - drive.encoderForward(25.0, 1.0)

        - ArmPositionMiddleCmd().dontBlock()

        // move to the B square
        - drive.encoderTiltForwardLeft(47.0, 0.45)
        - dropWobble()

        /* SHOOTING RINGS */

        // rotate torwards the high goal/power shots
        //- drive.rotate(Rot2d.degrees(-180.0), 0.6)
        - drive.rotate(Rot2d.degrees(-194.0), 0.6)

        // shoot the 3 rings
        //- shootRings() // high goal
        - shootRingsPowershot(0.33)

        /* GRABBING THE SECOND WOBBLE GOAL */

        - drive.rotate(Rot2d.degrees(-90.0), 0.7)

        - wobbleMiddleOpen().dontBlock()

        // drive torwards the 2nd wobble goal hopefully
        - drive.encoderForward(40.0, 0.3)
        // wait for the wobble goal to be grabbed
        - grabWobble()

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