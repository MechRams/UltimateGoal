package org.firstinspires.ftc.teamcode.commander.subsystem

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
) : DeltaSubsystem() {

    val motorType = HDHex_Motor_Only
    val maxRpm = motorType.GEAR_RATIO.inputRPM
    val maxTps = maxRpm * motorType.TICKS_PER_REVOLUTION

    init {
        defaultCommand = ShooterStopCmd()

        val coeffs = leftMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER)

        Constants.run {
            shooterP = coeffs.p
            shooterI = coeffs.i
            shooterD = coeffs.d
            shooterF = coeffs.f
        }

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

    init {
        defaultCommand = ShooterFlickOutCmd()
    }

    override fun loop() { }

}