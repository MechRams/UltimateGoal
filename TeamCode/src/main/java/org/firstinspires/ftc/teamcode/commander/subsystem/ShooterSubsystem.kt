package org.firstinspires.ftc.teamcode.commander.subsystem

import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.arcrobotics.ftclib.hardware.motors.MotorGroup
import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.github.serivesmejia.deltadrive.motors.revrobotics.HDHex_Motor_Only
import org.firstinspires.ftc.teamcode.commander.command.shooter.CmdShooterStop

class ShooterSubsystem(leftMotor: MotorEx, rightMotor: MotorEx) : DeltaSubsystem()  {

    var motorType = HDHex_Motor_Only
    var shooterMotors = MotorGroup(leftMotor, rightMotor)

    init {
        defaultCommand = CmdShooterStop(this)
    }

    override fun loop() {}

}