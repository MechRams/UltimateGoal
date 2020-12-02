package org.firstinspires.ftc.teamcode.commander.subsystem

import com.arcrobotics.ftclib.hardware.motors.MotorEx

import com.github.serivesmejia.deltautils.deltacommander.DeltaCommand
import com.github.serivesmejia.deltautils.deltacommander.DeltaSubsystem
import com.github.serivesmejia.deltautils.deltadrive.motors.revrobotics.HDHex_Motor_Only
import com.github.serivesmejia.deltautils.deltapid.PIDCoefficients
import com.github.serivesmejia.deltautils.deltapid.PIDController
import org.firstinspires.ftc.teamcode.commander.command.shooter.CmdShooterStop

class ShooterSubsystem(val leftMotor: MotorEx, val rightMotor: MotorEx) : DeltaSubsystem()  {

    var motorType = HDHex_Motor_Only

    private var pidCoefficients = PIDCoefficients(0.00152, 0.0, 0.0)
    private var pidController = PIDController(pidCoefficients)

    private var targetTPS = 0.0

    init {
        updateController()
        setDefaultCommand(CmdShooterStop(this))
    }

    override fun loop() {

        val avgVelocity = (leftMotor.velocity * rightMotor.velocity) / 2
        val power = pidController.calculate(avgVelocity)

        leftMotor.motorEx.power = power
        rightMotor.motorEx.power = power

    }

    private fun updateController() {

        pidController.setSetpoint(targetTPS)
                     .setDeadzone(0.1)
                     .setErrorTolerance(20.0)
                     .setInitialPower(1.0)

    }

    //ticks per second
    fun setTargetTPS(tps: Double) {
        targetTPS = tps
        updateController()
    }

    //revs per second
    fun setTargetRPS(rps: Double) = setTargetTPS(rps / motorType.TICKS_PER_REVOLUTION)

    //revs per minute
    fun setTargetRPM(rpm: Double) = setTargetRPS(rpm * 60)

}