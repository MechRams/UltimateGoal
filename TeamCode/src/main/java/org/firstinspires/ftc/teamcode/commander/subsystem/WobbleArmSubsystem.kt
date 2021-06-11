package org.firstinspires.ftc.teamcode.commander.subsystem

import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.github.serivesmejia.deltadrive.motors.andymark.NeveRest_Classic_40
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.CmdArmPositionStop
import kotlin.math.round

class WobbleArmSubsystem(val wobbleArmMotor: MotorEx, val wobbleClawServo: Servo) : DeltaSubsystem() {

    var armMoving = false

    companion object {
        val ARM_SAVE_POSITION = 0
        val ARM_MIDDLE_POSITION = round(NeveRest_Classic_40.TICKS_PER_REVOLUTION / 4.0).toInt()
        val ARM_UP_POSITION = round(NeveRest_Classic_40.TICKS_PER_REVOLUTION / 2.0).toInt()
    }

    init {
        defaultCommand = CmdArmPositionStop()
    }

    override fun loop() {
        if(armMoving) {
            wobbleArmMotor.set(0.8)
        }
    }

}