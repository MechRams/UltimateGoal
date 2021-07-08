package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import com.github.serivesmejia.deltacontrol.PIDFCoefficients
import com.github.serivesmejia.deltadrive.motors.andymark.NeveRest_Classic_60
import kotlin.math.roundToInt

@Config
object Constants {

    @JvmField var rotateP = 0.028
    @JvmField var rotateI = 0.002
    @JvmField var rotateD = 0.1
    @JvmField var rotateF = 0.0

    val rotatePIDFCoefficients get() = PIDFCoefficients(
        rotateP, rotateI,
        rotateD, rotateF
    )

    @JvmField var driveP = 0.09
    @JvmField var driveI = 0.0002
    @JvmField var driveD = 0.05
    @JvmField var driveF = 0.0

    val drivePIDFCoefficients get() = PIDFCoefficients(
            driveP, driveI,
            driveD, driveF
    )

    @JvmField var armP = 0.008
    @JvmField var armPositionStep = 5.5
    @JvmField var armPower = 0.20

    @JvmField var armSavePosition = -60
    @JvmField var armMiddlePosition = -620
    @JvmField var armUpPosition = -297

    @JvmField var shooterP = 0.05
    @JvmField var shooterI = 0.0
    @JvmField var shooterD = 0.0
    @JvmField var shooterF = 0.0
    @JvmField var shooterHighGoalPower = 0.387
    @JvmField var shooterPowerShotPower = 0.28

    @JvmField var flickerInPos = 0.83
    @JvmField var flickerOutPos = 0.98

    var lastOpMode = OpModeType.TELEOP

}