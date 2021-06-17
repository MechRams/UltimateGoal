package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import com.github.serivesmejia.deltacontrol.PIDFCoefficients
import com.github.serivesmejia.deltadrive.motors.andymark.NeveRest_Classic_60
import kotlin.math.roundToInt

@Config
object Constants {

    @JvmField var rotateP = 0.039;
    @JvmField var rotateI = 0.0005;
    @JvmField var rotateD = 0.005;
    @JvmField var rotateF = 0.0028;

    val rotatePIDFCoefficients get() = PIDFCoefficients(
        rotateP, rotateI,
        rotateD, rotateF
    )

    @JvmField var armP = 0.008
    @JvmField var armPositionIncrement = 5.5
    @JvmField var armPower = 0.33

    @JvmField var armSavePosition = 0
    @JvmField var armMiddlePosition = -633
    @JvmField var armUpPosition = -297

    @JvmField var shooterPower = 0.8

    var lastOpMode = OpModeType.TELEOP

}