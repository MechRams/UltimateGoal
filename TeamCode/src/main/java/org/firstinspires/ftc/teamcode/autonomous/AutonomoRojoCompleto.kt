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

        while(opModeIsActive()) {
            deltaHdw.clearBulkCache()
            deltaScheduler.update()
        }
    }

    fun stackA() = deltaSequence {
        - drive.encoderTiltForwardRight(30.0, 0.3, 4.0)
        - drive.encoderForward(10.0, 0.3, 4.0)

        val armMiddle = ArmPositionMiddleCmd()

        - drive.encoderForward(50.5, 0.3, 4.0).markers {
            distanceMarker(10.0) { + armMiddle }
        }

        - dropWobble(armMiddle)

        - drive.encoderStrafeLeft(30.0, 0.3, 4.0)
        - drive.rotate(Rot2d.degrees(180.0), 0.3, 3.0)
        - shootRings()

        - drive.encoderForward(30.0, 0.2, 4.0)
    }

    fun stackB() = deltaSequence {
        - shootRings()
    }

    fun stackC() = deltaSequence {
        - shootRings()
    }

}