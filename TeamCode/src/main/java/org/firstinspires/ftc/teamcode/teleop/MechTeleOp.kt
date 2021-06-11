package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltaevent.gamepad.button.Button
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.commander.command.drive.DriveJoystickCmd
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.CmdIntakeConveyIn
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.CmdIntakeConveyOut
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.CmdIntakeConveyStop
import org.firstinspires.ftc.teamcode.commander.command.shooter.CmdShooterRun
import org.firstinspires.ftc.teamcode.commander.command.shooter.CmdShooterStop
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.CmdArmPositionMiddle
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.CmdArmPositionSave
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.CmdArmPositionUp

@TeleOp(name="TeleOp", group="Final")
class MechTeleOp : MechOpMode(usingIMU = false) {

    override fun run() {
        telemetry = MultipleTelemetry(FtcDashboard.getInstance().telemetry, telemetry)

        // programar el comando que controlara el chassis con el gamepad
        + DriveJoystickCmd(gamepad1)

        superGamepad1.scheduleOn(Button.A,
            // encender hacia adentro el intake cuando se presiona A
            CmdIntakeConveyIn(),
            // apagar el intake cuando se deja de presionar A
            CmdIntakeConveyStop()
        )

        superGamepad1.scheduleOn(Button.B,
            // encender hacia afuera el intake cuando se presiona B
            CmdIntakeConveyOut(),
            // apagar el intake cuando se deja de presionar B
            CmdIntakeConveyStop()
        )

        superGamepad1.scheduleOn(Button.X,
            // encender el shooter cuando se presiona X
            // usar los triggers para desacelerar
            CmdShooterRun(1.0),
            //comando para cuando se deja de presionar X
            CmdShooterStop()
        )

        //controlar el brazo para el wobble

        //mover el brazo arriba con el dpad up
        superGamepad1.scheduleOnPress(Button.DPAD_UP,
            CmdArmPositionUp()
        )
        //mover el brazo enmedio con el boton dpad right
        superGamepad1.scheduleOnPress(Button.DPAD_RIGHT,
            CmdArmPositionMiddle()
        )
        //meter el brazo con el boton dpad down
        superGamepad1.scheduleOnPress(Button.DPAD_DOWN,
            CmdArmPositionSave()
        )

        superGamepad1.attachToScheduler()

        waitForStart()

        while(opModeIsActive()) {
            telemetry.addData("fl", hdw.wheelFrontLeft.power)
            telemetry.addData("fr", hdw.wheelFrontRight.power)
            telemetry.addData("bl", hdw.wheelBackLeft.power)
            telemetry.addData("br", hdw.wheelBackRight.power)
            telemetry.update()

            deltaScheduler.update()
        }
    }

}