package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltautils.drive.extendable.linearopmode.holonomic.IMUPIDEncoderHolonomicLinearOpMode
import com.github.serivesmejia.deltautils.drive.motors.andymark.NeveRest_Orbital_20
import com.github.serivesmejia.deltautils.drive.utils.gear.TwoGearRatio
import com.github.serivesmejia.deltautils.pid.PIDCoefficients
import org.firstinspires.ftc.teamcode.hardware.Hardware

class AutonomoBase : IMUPIDEncoderHolonomicLinearOpMode() {

    val hdw = Hardware()

    override fun _runOpMode() {

        setRotatePID(PIDCoefficients(0.0152, 0.0, 0.0))
        imuParameters.DEAD_ZONE = 0.09

        encoderParameters.TICKS_PER_REV = NeveRest_Orbital_20.TICKS_PER_REVOLUTION
        encoderParameters.DRIVE_GEAR_REDUCTION = TwoGearRatio(1, 1)
        encoderParameters.WHEEL_DIAMETER_INCHES = 4.0

        runAutonomous()

    }

    open fun runAutonomous() {}

    override fun setup() {

        hdw.initHardware(hardwareMap, false)

        frontLeft = hdw.wheelFrontLeft
        frontRight = hdw.wheelFrontRight
        backLeft = hdw.wheelBackLeft
        backRight = hdw.wheelBackRight

    }

}