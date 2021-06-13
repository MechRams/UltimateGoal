package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import com.github.serivesmejia.deltacontrol.PIDFCoefficients

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

}