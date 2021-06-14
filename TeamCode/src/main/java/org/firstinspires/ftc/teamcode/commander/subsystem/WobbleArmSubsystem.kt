package org.firstinspires.ftc.teamcode.commander.subsystem

import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.ArmPositionStopCmd
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.claw.ArmClawCloseCmd

class WobbleArmSubsystem(val wobbleArmMotor: MotorEx) : DeltaSubsystem() {

    var armMoving = false

    init {
        defaultCommand = ArmPositionStopCmd()
        wobbleArmMotor.encoder.reset()
    }

    override fun loop() {
        wobbleArmMotor.positionCoefficient = Constants.armP

        if(armMoving) {
            wobbleArmMotor.set(Constants.armPower)
        }
    }

}

class WobbleArmClawSubsystem(val wobbleClawServo: Servo) : DeltaSubsystem() {

    init {
        defaultCommand = ArmClawCloseCmd()
    }

    override fun loop() { }

}