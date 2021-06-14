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
    @JvmField var armPower = 0.33

    @JvmField var ARM_SAVE_POSITION = 0
    @JvmField var ARM_MIDDLE_POSITION = -633
    @JvmField var ARM_UP_POSITION = -297

}