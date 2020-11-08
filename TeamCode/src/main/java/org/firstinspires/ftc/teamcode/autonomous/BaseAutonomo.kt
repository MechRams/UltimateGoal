package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltautils.deltadrive.extendable.linearopmode.holonomic.IMUPIDEncoderHolonomicLinearOpMode
import com.github.serivesmejia.deltautils.deltadrive.motors.andymark.NeveRest_Orbital_20
import com.github.serivesmejia.deltautils.deltadrive.utils.gear.TwoGearRatio
import com.github.serivesmejia.deltautils.deltapid.PIDCoefficients

import org.firstinspires.ftc.teamcode.hardware.Hardware

open class BaseAutonomo : IMUPIDEncoderHolonomicLinearOpMode() {

    val hdw = Hardware()

    override fun _runOpMode() {

        setRotatePID(PIDCoefficients(0.0152, 0.0, 0.0)) //definir constantes PID
        imuParameters.DEAD_ZONE = 0.09 //el power minimo con el que se puede mover el robot

        encoderParameters.TICKS_PER_REV = NeveRest_Orbital_20.TICKS_PER_REVOLUTION
        encoderParameters.DRIVE_GEAR_REDUCTION = TwoGearRatio(1.0, 1.0)
        encoderParameters.WHEEL_DIAMETER_INCHES = 4.0

        runAutonomous()

    }

    open fun runAutonomous() {}

    override fun setup() {

        hdw.initHardware(hardwareMap)

        frontLeft = hdw.wheelFrontLeft
        frontRight = hdw.wheelFrontRight
        backLeft = hdw.wheelBackLeft
        backRight = hdw.wheelBackRight

    }

}