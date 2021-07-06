package org.firstinspires.ftc.teamcode.autonomous.test

import com.github.serivesmejia.deltacommander.command.DeltaSequentialCmd
import com.github.serivesmejia.deltacommander.command.DeltaWaitCmd
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltacommander.dsl.deltaSequence
import com.github.serivesmejia.deltamath.geometry.Rot2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.OpModeType
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.IntakeConveyInCmd
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.IntakeConveyStopCmd

@Autonomous(name = "Drive Forward", group = "test", preselectTeleOp = "TeleOp")
class AutonomoDriveForward : MechOpMode(OpModeType.AUTO) {

    override fun run() {
        waitForStart()

        + autoA()

        while(opModeIsActive()) {
            deltaScheduler.update()
        }
    }

    fun autoA() = deltaSequence {
        - drive.encoderForward(9999.0, 0.2, 9999.0)
    }

}