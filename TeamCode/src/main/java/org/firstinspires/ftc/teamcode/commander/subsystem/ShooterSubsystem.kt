package org.firstinspires.ftc.teamcode.commander.subsystem

import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.arcrobotics.ftclib.hardware.motors.MotorGroup
import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.github.serivesmejia.deltadrive.motors.revrobotics.HDHex_Motor_Only
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterFlickOutCmd
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterStopCmd

class ShooterSubsystem(
    val leftMotor: MotorEx,
    val rightMotor: MotorEx
) : DeltaSubsystem()  {

    var motorType = HDHex_Motor_Only
    var shooterMotors = MotorGroup(leftMotor, rightMotor)

    init {
        defaultCommand = ShooterStopCmd()
    }

    override fun loop() {
        leftMotor.motorEx.setPIDFCoefficients(
            DcMotor.RunMode.RUN_USING_ENCODER,
            PIDFCoefficients(Constants.shooterP, Constants.shooterI, Constants.shooterD, Constants.shooterF)
        )

        rightMotor.motorEx.setPIDFCoefficients(
            DcMotor.RunMode.RUN_USING_ENCODER,
            PIDFCoefficients(Constants.shooterP, Constants.shooterI, Constants.shooterD, Constants.shooterF)
        )
    }

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