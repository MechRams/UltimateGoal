package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltaevent.gamepad.button.Button
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.OpModeType
import org.firstinspires.ftc.teamcode.commander.command.drive.DriveJoystickCmd
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.IntakeConveyInCmd
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.IntakeConveyOutCmd
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.IntakeConveyStopCmd
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterFlickCmd
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterFlickOutCmd
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterRunCmd
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterStopCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionMiddleCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionResetCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionSaveCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionUpCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawCloseCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawOpenCmd

@TeleOp(name="TeleOp", group="Final")
class MechTeleOp : MechOpMode(OpModeType.TELEOP, usingIMU = false) {

    override fun run() {
        // programar el comando que controlara el chassis con el gamepad
        + DriveJoystickCmd(gamepad1)

        superGamepad1.scheduleOn(Button.A,
            // encender hacia adentro el intake cuando se presiona A
            IntakeConveyInCmd(),
            // apagar el intake cuando se deja de presionar A
            IntakeConveyStopCmd()
        )

        superGamepad1.scheduleOn(Button.B,
            // encender hacia afuera el intake cuando se presiona B
            IntakeConveyOutCmd(),
            // apagar el intake cuando se deja de presionar B
            IntakeConveyStopCmd()
        )

        superGamepad1.toggleScheduleOn(Button.X,
            // encender el shooter cuando se presiona X
            // usar los triggers para desacelerar
            ShooterRunCmd(1.0),
            //comando para cuando se vuelva a presionar X
            ShooterStopCmd()
        )

        superGamepad1.scheduleOn(Button.Y,
            // mover el servo adentro y afuera constantemente
            // mientras se este presionando Y
            ShooterFlickCmd(),
            // dejar de mover el servo cuando se deja de presionar Y
            ShooterFlickOutCmd()
        )

        //controlar el brazo para el wobble

        //meter el brazo con el boton dpad down
        superGamepad1.scheduleOnPress(Button.DPAD_LEFT,
            ArmPositionSaveCmd()
        )
        //mover el brazo arriba con el dpad up
        superGamepad1.scheduleOnPress(Button.DPAD_UP,
            ArmPositionUpCmd()
        )
        //mover el brazo enmedio con el boton dpad right
        superGamepad1.scheduleOnPress(Button.DPAD_RIGHT,
            ArmPositionMiddleCmd()
        )

        // garra toggle con dpad
        superGamepad1.toggleScheduleOn(Button.DPAD_DOWN,
            ArmClawOpenCmd(),
            ArmClawCloseCmd()
        )

        ArmPositionResetCmd().schedule(false)

        superGamepad1.attachToScheduler()

        waitForStart()

        while(opModeIsActive()) {
            telemetry.addData("fl", hdw.wheelFrontLeft.power)
            telemetry.addData("fr", hdw.wheelFrontRight.power)
            telemetry.addData("bl", hdw.wheelBackLeft.power)
            telemetry.addData("br", hdw.wheelBackRight.power)
            telemetry.addData("in", hdw.motorIntakeConvey.power)
            telemetry.addData("wa pos", hdw.motorWobbleArm.currentPosition)
            telemetry.addData("wa claw pos", hdw.servoWobbleClaw.position)
            telemetry.update()

            deltaScheduler.update()
        }
    }

}