package org.firstinspires.ftc.teamcode.teleop

import com.github.serivesmejia.deltautils.deltadrive.extendable.linearopmode.holonomic.JoystickHolonomicLinearOpMode;
import com.github.serivesmejia.deltautils.deltaevent.event.gamepad.SuperGamepadEvent
import com.github.serivesmejia.deltautils.deltacommander.DeltaScheduler
import com.github.serivesmejia.deltautils.deltaevent.gamepad.GamepadDataPacket
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Hardware

@TeleOp(name="TeleOp", group="Final")
class TeleOp : JoystickHolonomicLinearOpMode() {

    private val hdw = Hardware()

    override fun _runOpMode() {

        //EMPIEZA CODIGO DEL START A

        superGamepad1.registerEvent(object: SuperGamepadEvent() {

            override fun loop(gdp: GamepadDataPacket) {
                joystick(gdp.gamepad, true, 0.7)
            }

        })

        //EMPIEZA CODIGO DEL START B

        superGamepad2.registerEvent(object: SuperGamepadEvent() {

        })

        waitForStart()
        
        while(opModeIsActive()) {

            superGamepad1.update()
            superGamepad2.update()

        }

    }

    override fun setup() {

        hdw.initHardware(hardwareMap, false)

        frontLeft = hdw.wheelFrontLeft
        frontRight = hdw.wheelFrontRight
        backLeft = hdw.wheelBackLeft
        backRight = hdw.wheelBackRight

    }

}