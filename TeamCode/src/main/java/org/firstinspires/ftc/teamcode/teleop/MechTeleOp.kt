package org.firstinspires.ftc.teamcode.teleop

import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltaevent.gamepad.button.Button
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Constants
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
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.*
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawCloseCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawOpenCmd
import kotlin.math.roundToInt

@TeleOp(name="TeleOp", group="Final")
class MechTeleOp : MechOpMode(OpModeType.TELEOP, usingIMU = false) {

    private var armPos = 0.0

    override fun run() {
        telemetry.msTransmissionInterval = 5

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
            ShooterRunCmd {
                if(gamepad1.left_trigger >= 0.3 || gamepad1.right_trigger >= 0.3)
                    // aplicar menos velocidad al shooter cuando se presionan los trigers
                    Constants.shooterPowerShotPower
                else
                    // aplicar velocidad normal cuando no son presionados
                    Constants.shooterHighGoalPower
            },
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
        superGamepad1 {
            Button.DPAD_LEFT.pressing {
                armPos += Constants.armPositionStep
            }

            Button.DPAD_RIGHT.pressing {
                armPos -= Constants.armPositionStep
            }
        }

        // agregar comando que constantemente definira la posicion actual del brazo
        + ArmPositionRunCmd { armPos.roundToInt() }

        // garra toggle con dpad
        superGamepad1.toggleScheduleOn(Button.DPAD_DOWN,
            ArmClawOpenCmd(), // abrir el servo cuando se presiona una vez el dpad
            ArmClawCloseCmd() // cerrar el servo cuando se presiona otra vez el dpad
        )

        superGamepad1.attachToScheduler()

        waitForStart()

        // get the initial arm position from the current pos (after starting the opmode)
        armPos = hdw.motorWobbleArm.currentPosition.toDouble()

        // bulk reads
        deltaHdw.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        while(opModeIsActive()) {
            deltaHdw.clearBulkCache()

            deltaScheduler.update()

            telemetry.addData("fl", hdw.wheelFrontLeft.power)
            telemetry.addData("fr", hdw.wheelFrontRight.power)
            telemetry.addData("bl", hdw.wheelBackLeft.power)
            telemetry.addData("br", hdw.wheelBackRight.power)
            telemetry.addData("flick", hdw.servoShooterFlicker.position)

            telemetry.addData("in", hdw.motorIntakeConvey.power)
            telemetry.addData("wa claw pos", hdw.servoWobbleClaw.position)
            telemetry.update()
        }
    }

}