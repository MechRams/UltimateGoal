package org.firstinspires.ftc.teamcode

import com.github.serivesmejia.deltadrive.hardware.DeltaHardwareHolonomic
import com.github.serivesmejia.deltadrive.utils.Invert
import com.github.serivesmejia.deltaevent.gamepad.SuperGamepad
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.commander.Subsystems
import org.firstinspires.ftc.teamcode.hardware.Hardware

abstract class MechOpMode : LinearOpMode() {

    val hdw = Hardware()
    lateinit var deltaHdw: DeltaHardwareHolonomic

    val subsystems = Subsystems()

    lateinit var superGamepad1: SuperGamepad
    lateinit var superGamepad2: SuperGamepad

    override fun runOpMode() {
        superGamepad1 = SuperGamepad(gamepad1) //crear dos "super gamepads" con los gamepads originals
        superGamepad2 = SuperGamepad(gamepad2)

        hdw.initHardware(hardwareMap) //inicializamos el hardware
        subsystems.init(hdw) //inicializamos todos los subsistemas/mecanismos

        deltaHdw = DeltaHardwareHolonomic(hardwareMap, Invert.RIGHT_SIDE)
        deltaHdw.initHardware(
                hdw.wheelFrontLeft,
                hdw.wheelFrontRight,
                hdw.wheelBackLeft,
                hdw.wheelBackRight,
                true
        )

        run()
    }

    abstract fun run()

}