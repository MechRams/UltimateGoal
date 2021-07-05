package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltacommander.command.DeltaRunCmd
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltacommander.dsl.deltaSequence
import com.github.serivesmejia.deltamath.geometry.Rot2d
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.OpModeType
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionMiddleCmd
import org.firstinspires.ftc.teamcode.vision.RingPipeline2

@Autonomous
class AutonomoRotateTest : MechOpMode(OpModeType.AUTO) {

    override fun run() {
        waitForStart()
        + rotateInfinitely()

        while(opModeIsActive()) {
            deltaScheduler.update()
            if(deltaScheduler.commandsAmount <= 0) {
                + rotateInfinitely()
            }
        }
    }

    fun rotateInfinitely() = deltaSequence {
        - drive.rotate(Rot2d.degrees(90.0), 0.4, 10.0)
        - drive.rotate(Rot2d.degrees(-90.0), 0.4, 10.0)
    }

}