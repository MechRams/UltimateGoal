package org.firstinspires.ftc.teamcode.teleop

import com.github.serivesmejia.deltacommander.DeltaScheduler
import com.github.serivesmejia.deltadrive.drivebase.MecanumDrive
import com.github.serivesmejia.deltaevent.event.gamepad.SuperGamepadEvent
import com.github.serivesmejia.deltaevent.gamepad.GamepadDataPacket
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
        DeltaScheduler.instance.schedule(DriveJoystickCmd(subsystems.drive, gamepad1))

        superGamepad1.attachToScheduler()

        // EMPIEZA CODIGO DEL START B

        superGamepad2.scheduleOn(Button.A,
            // encender hacia adentro el intake cuando se presiona A
            CmdIntakeConveyIn(subsystems.intakeConvey),
            // apagar el intake cuando se deja de presionar A
            CmdIntakeConveyStop(subsystems.intakeConvey)
        )

        superGamepad2.scheduleOn(Button.B,
            // encender hacia afuera el intake cuando se presiona B
            CmdIntakeConveyOut(subsystems.intakeConvey),
            // apagar el intake cuando se deja de presionar B
            CmdIntakeConveyStop(subsystems.intakeConvey)
        )

        superGamepad2.scheduleOn(Button.X,
            // encender el shooter cuando se presiona X
            // usar los triggers para desacelerar
            CmdShooterRun(subsystems.shooter) { 1.0 - eitherTrigger(gamepad2) },
            //comando para cuando se deja de presionar X
            CmdShooterStop(subsystems.shooter)
        )

        //controlar el brazo para el wobble

        //mover el brazo arriba con el dpad up
        superGamepad2.scheduleOnPress(
            Button.DPAD_UP,
            CmdArmPositionUp(subsystems.wobbleArm)
        )
        //mover el brazo enmedio con el boton dpad right
        superGamepad2.scheduleOnPress(
            Button.DPAD_RIGHT,
            CmdArmPositionMiddle(subsystems.wobbleArm)
        )
        //meter el brazo con el boton dpad right
        superGamepad2.scheduleOnPress(
            Button.DPAD_DOWN,
            CmdArmPositionSave(subsystems.wobbleArm)
        )

        superGamepad2.attachToScheduler()

        waitForStart()

        while(opModeIsActive()) {
            DeltaScheduler.instance.update()
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