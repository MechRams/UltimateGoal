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
        - drive.encoderTiltForwardRight(50.0, 1.0, 4.0)

        - ArmPositionMiddleCmd().dontBlock()

        - drive.encoderForward(25.0, 1.0, 4.0)

        - dropWobble()

        //- drive.encoderStrafeLeft(30.0, 0.3, 4.0)
        - drive.rotate(Rot2d.degrees(200.0), 0.7)
        - shootRings()

        - drive.rotate(Rot2d.degrees(-40.0), 0.7)

        val driveForwardCmd = drive.encoderForward(30.0, 0.5, 4.0).command
        val grabWobbleCmd = grabWobble(driveForwardCmd)

        - grabWobbleCmd.dontBlock()
        - driveForwardCmd
        - grabWobbleCmd.waitFor()

        - drive.rotate(Rot2d.degrees(90.0), 0.6)

        - ArmPositionMiddleCmd().dontBlock()

        - drive.encoderForward(30.0, 1.0)
        - dropWobble()
    }

    fun stackB() = deltaSequence {
        - shootRings()
    }

    fun stackC() = deltaSequence {
        - shootRings()
    }

}