package org.firstinspires.ftc.teamcode.commander.subsystem

import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.arcrobotics.ftclib.hardware.motors.MotorGroup
import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.github.serivesmejia.deltadrive.motors.revrobotics.HDHex_Motor_Only
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterFlickOutCmd
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterStopCmd

class ShooterSubsystem(
    val leftMotor: DcMotorEx,
    val rightMotor: DcMotorEx
) : DeltaSubsystem()  {

    val motorType = HDHex_Motor_Only
    val maxRpm = motorType.GEAR_RATIO.inputRPM
    val maxTps = maxRpm * motorType.TICKS_PER_REVOLUTION

    init {
        defaultCommand = ShooterStopCmd()
        updateCoefficients()
    }

    fun updateCoefficients() = Constants.run{
        leftMotor.setPIDFCoefficients(
            DcMotor.RunMode.RUN_USING_ENCODER,
            PIDFCoefficients(
                shooterP, shooterI, shooterD, shooterF
            )
        )

        rightMotor.setPIDFCoefficients(
            DcMotor.RunMode.RUN_USING_ENCODER,
            PIDFCoefficients(
                shooterP, shooterI, shooterD, shooterF
            )
        )
    }

    override fun loop() { }

}

class ShooterFlickerSubsystem(
    val flickerServo: Servo
) : DeltaSubsystem() {

    companion object {
        const val FLICKER_IN_POS = 0.3
        const val FLICKER_OUT_POS = 1.0
    }

    init {
        defaultCommand = ShooterFlickOutCmd()
    }

    override fun loop() { }

}