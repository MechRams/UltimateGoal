package org.firstinspires.ftc.teamcode.teleop

import com.github.serivesmejia.deltautils.drive.extendable.linearopmode.holonomic.JoystickHolonomicLinearOpMode;
import com.github.serivesmejia.deltautils.event.event.gamepad.SuperGamepadEvent
import com.github.serivesmejia.deltautils.event.gamepad.GamepadDataPacket
import com.github.serivesmejia.deltautils.event.gamepad.button.Buttons
import org.firstinspires.ftc.teamcode.hardware.Hardware

class TeleOp : JoystickHolonomicLinearOpMode() {

    val hdw = Hardware()

    override fun _runOpMode() {

        //EMPIEZA CODIGO DEL START A

        superGamepad1?.registerEvent(object: SuperGamepadEvent() {

            override fun loop(gdp: GamepadDataPacket) {
                joystick(gdp.gamepad, true, 0.7)
            }

        })

        //EMPIEZA CODIGO DEL START B

        superGamepad2?.registerEvent(object: SuperGamepadEvent() {

        })

        waitForStart()

    }

    override fun setup() {

        hdw.initHardware(hardwareMap, false)

        frontLeft = hdw.wheelFrontLeft
        frontRight = hdw.wheelFrontRight
        backLeft = hdw.wheelBackLeft
        backRight = hdw.wheelBackRight

    }

}