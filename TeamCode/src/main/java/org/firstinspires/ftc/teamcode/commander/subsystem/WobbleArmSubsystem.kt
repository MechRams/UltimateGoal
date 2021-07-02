package org.firstinspires.ftc.teamcode.commander.subsystem

import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.qualcomm.robotcore.hardware.DigitalChannel
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionStopCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawCloseCmd

class WobbleArmSubsystem(
    val wobbleArmMotor: MotorEx,
    //val wobbleTouchSensor: TouchSensor,
    val isAutonomous: Boolean
) : DeltaSubsystem() {

    var armMoving = false

    init {
        defaultCommand = ArmPositionStopCmd()
        wobbleArmMotor.resetEncoder()
    }

    override fun loop() {
        wobbleArmMotor.positionCoefficient = Constants.armP

        if(armMoving) {
            wobbleArmMotor.set(Constants.armPower)
        } else {
            wobbleArmMotor.set(0.0)
        }
    }

}

class WobbleArmClawSubsystem(val wobbleClawServo: Servo) : DeltaSubsystem() {
    init {
        defaultCommand = ArmClawCloseCmd()
    }

    override fun loop() { }
}