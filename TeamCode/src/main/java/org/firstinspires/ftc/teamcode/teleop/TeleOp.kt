package org.firstinspires.ftc.teamcode.teleop

import com.github.serivesmejia.deltaevent.event.gamepad.SuperGamepadEvent
import com.github.serivesmejia.deltacommander.DeltaScheduler
import com.github.serivesmejia.deltadrive.HolonomicDrivebase
import com.github.serivesmejia.deltadrive.drivebase.MecanumDrive
import com.github.serivesmejia.deltadrive.hardware.DeltaHardwareHolonomic
import com.github.serivesmejia.deltadrive.utils.Invert

import com.github.serivesmejia.deltaevent.gamepad.GamepadDataPacket
import com.github.serivesmejia.deltaevent.gamepad.SuperGamepad
import com.github.serivesmejia.deltaevent.gamepad.button.Button
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.commander.Subsystems

import org.firstinspires.ftc.teamcode.commander.command.wobblearm.*
import org.firstinspires.ftc.teamcode.commander.subsystem.*
import org.firstinspires.ftc.teamcode.hardware.Hardware

@TeleOp(name="TeleOp", group="Final")
class TeleOp : MechOpMode() {

    override fun run() {
        val mecanumDrive = MecanumDrive(deltaHdw)

        //EMPIEZA CODIGO DEL START A
        superGamepad1.attachToScheduler()

        superGamepad1.registerEvent(object: SuperGamepadEvent() {
            override fun loop(gdp: GamepadDataPacket) {
                mecanumDrive.joystickRobotCentric(gdp.gamepad, true, 0.7)
            }
        })

        //EMPIEZA CODIGO DEL START B
        superGamepad2.attachToScheduler()

        superGamepad2.scheduleOnPress(Button.DPAD_UP, CmdArmPositionUp(subsystems.wobbleArm))
        superGamepad2.scheduleOnPress(Button.DPAD_DOWN, CmdArmPositionSave(subsystems.wobbleArm))

        waitForStart()

        while(opModeIsActive()) {
            DeltaScheduler.instance.update()
        }
    }

}