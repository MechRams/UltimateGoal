package org.firstinspires.ftc.teamcode.teleop

import com.github.serivesmejia.deltautils.deltadrive.extendable.linearopmode.holonomic.JoystickHolonomicLinearOpMode;
import com.github.serivesmejia.deltautils.deltaevent.event.gamepad.SuperGamepadEvent
import com.github.serivesmejia.deltautils.deltacommander.DeltaScheduler
import com.github.serivesmejia.deltautils.deltaevent.gamepad.GamepadDataPacket
import com.github.serivesmejia.deltautils.deltaevent.gamepad.button.Button
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.CmdArmPositionUp
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterSubsystem
import org.firstinspires.ftc.teamcode.commander.subsystem.WobbleArmSubsystem
import org.firstinspires.ftc.teamcode.hardware.Hardware

@TeleOp(name="TeleOp", group="Final")
class TeleOp : JoystickHolonomicLinearOpMode() {

    private val hdw = Hardware()

    override fun _runOpMode() {

        val armSubsystem = WobbleArmSubsystem(hdw.motorWobbleArm, hdw.servoWobbleClaw)
        val shooterSubsystem = ShooterSubsystem(hdw.motorShooterLeft, hdw.motorShooterRight)

        DeltaScheduler.instance.addSubsystem(armSubsystem)
        DeltaScheduler.instance.addSubsystem(shooterSubsystem)

        //EMPIEZA CODIGO DEL START A
        superGamepad1.registerEvent(object: SuperGamepadEvent() {
            override fun loop(gdp: GamepadDataPacket) {
                joystick(gdp.gamepad, true, 0.7)
            }
        })

        //EMPIEZA CODIGO DEL START B
        superGamepad2.registerEvent(object: SuperGamepadEvent() {

        })

        superGamepad2.scheduleOnPress(Button.DPAD_UP, CmdArmPositionUp(armSubsystem))

        superGamepad1.attachToScheduler()
        superGamepad2.attachToScheduler()

        waitForStart()
        
        while(opModeIsActive()) {
            DeltaScheduler.instance.update()
        }

        DeltaScheduler.reset()

    }

    override fun setup() {

        hdw.initHardware(hardwareMap)

        frontLeft = hdw.wheelFrontLeft
        frontRight = hdw.wheelFrontRight
        backLeft = hdw.wheelBackLeft
        backRight = hdw.wheelBackRight

    }

}