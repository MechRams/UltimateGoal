package org.firstinspires.ftc.teamcode.teleop

import com.github.serivesmejia.deltautils.deltadrive.extendable.linearopmode.holonomic.JoystickHolonomicLinearOpMode;
import com.github.serivesmejia.deltautils.deltaevent.event.gamepad.SuperGamepadEvent
import com.github.serivesmejia.deltautils.deltacommander.DeltaScheduler

import com.github.serivesmejia.deltautils.deltaevent.gamepad.GamepadDataPacket
import com.github.serivesmejia.deltautils.deltaevent.gamepad.button.Button

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.commander.Subsystems

import org.firstinspires.ftc.teamcode.commander.command.wobblearm.*
import org.firstinspires.ftc.teamcode.commander.subsystem.*
import org.firstinspires.ftc.teamcode.hardware.Hardware

@TeleOp(name="TeleOp", group="Final")
class TeleOp : JoystickHolonomicLinearOpMode() {

    private val hdw = Hardware()
    private val subsystems = Subsystems()

    override fun _runOpMode() {
        subsystems.init(hdw) //inicializamos todos los subsistemas/mecanismos

        //EMPIEZA CODIGO DEL START A
        superGamepad1.registerEvent(object: SuperGamepadEvent() {
            override fun loop(gdp: GamepadDataPacket) {
                joystick(gdp.gamepad, true, 0.7)
            }
        })

        //EMPIEZA CODIGO DEL START B
        superGamepad2.scheduleOnPress(Button.DPAD_UP, CmdArmPositionUp(subsystems.wobbleArm))
        superGamepad2.scheduleOnPress(Button.DPAD_DOWN, CmdArmPositionSave(subsystems.wobbleArm))

        //general
        superGamepad1.attachToScheduler()
        superGamepad2.attachToScheduler()

        waitForStart()

        while(opModeIsActive()) {
            DeltaScheduler.instance.update()
        }

    }

    override fun setup() {
        hdw.initHardware(hardwareMap)

        frontLeft = hdw.wheelFrontLeft
        frontRight = hdw.wheelFrontRight
        backLeft = hdw.wheelBackLeft
        backRight = hdw.wheelBackRight
    }

}