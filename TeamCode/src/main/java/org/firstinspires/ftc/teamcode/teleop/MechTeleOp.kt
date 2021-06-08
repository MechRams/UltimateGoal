package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.github.serivesmejia.deltacommander.DeltaScheduler
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltaevent.gamepad.button.Button
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.commander.command.drive.DriveJoystickCmd
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.*
import org.firstinspires.ftc.teamcode.commander.command.shooter.*
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.*

@TeleOp(name="TeleOp", group="Final")
class MechTeleOp : MechOpMode() {

    override fun run() {
        telemetry = MultipleTelemetry(FtcDashboard.getInstance().telemetry, telemetry)

        deltaScheduler.schedule(DriveJoystickCmd(subsystems.drive, gamepad1))

        superGamepad1.scheduleOn(Button.A,
            // encender hacia adentro el intake cuando se presiona A
            CmdIntakeConveyIn(subsystems.intakeConvey),
            // apagar el intake cuando se deja de presionar A
            CmdIntakeConveyStop(subsystems.intakeConvey)
        )

        superGamepad1.scheduleOn(Button.B,
            // encender hacia afuera el intake cuando se presiona B
            CmdIntakeConveyOut(subsystems.intakeConvey),
            // apagar el intake cuando se deja de presionar B
            CmdIntakeConveyStop(subsystems.intakeConvey)
        )

        superGamepad1.scheduleOn(Button.X,
            // encender el shooter cuando se presiona X
            // usar los triggers para desacelerar
            CmdShooterRun(subsystems.shooter) { 1.0 - eitherTrigger(gamepad1) },
            //comando para cuando se deja de presionar X
            CmdShooterStop(subsystems.shooter)
        )

        //controlar el brazo para el wobble

        //mover el brazo arriba con el dpad up
        superGamepad1.scheduleOnPress(
            Button.DPAD_UP,
            CmdArmPositionUp(subsystems.wobbleArm)
        )
        //mover el brazo enmedio con el boton dpad right
        superGamepad1.scheduleOnPress(
            Button.DPAD_RIGHT,
            CmdArmPositionMiddle(subsystems.wobbleArm)
        )
        //meter el brazo con el boton dpad right
        superGamepad1.scheduleOnPress(
            Button.DPAD_DOWN,
            CmdArmPositionSave(subsystems.wobbleArm)
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

    fun eitherTrigger(gamepad: Gamepad): Double {
        //usar el valor del left trigger si esta presionado
        //si no esta presionado el left trigger, usar el valor del right trigger
        return if (gamepad.left_trigger > 0.2) {
            1.0 - gamepad.left_trigger.toDouble()
        } else {
            1.0 - gamepad.right_trigger.toDouble()
        }
    }

}