package org.firstinspires.ftc.teamcode.commander.subsystem

import com.github.serivesmejia.deltautils.deltacommander.DeltaSubsystem
import com.github.serivesmejia.deltautils.deltadrive.motors.andymark.NeveRest_Classic_40
import com.github.serivesmejia.deltautils.deltapid.PIDCoefficients
import com.github.serivesmejia.deltautils.deltapid.PIDController

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.CmdArmPositionSave
import org.firstinspires.ftc.teamcode.commander.command.wobblearm.CmdArmPositionStop

class WobbleArmSubsystem(val wobbleArmMotor: DcMotor, val wobbleClawServo: Servo) : DeltaSubsystem() {

    val armController = PIDController(PIDCoefficients(0.0000152, 0.0, 0.0))

    var armMoving = false

    val ARM_SAVE_POSITION = 0.0
    val ARM_MIDDLE_POSITION = NeveRest_Classic_40.TICKS_PER_REVOLUTION / 4.0
    val ARM_UP_POSITION = NeveRest_Classic_40.TICKS_PER_REVOLUTION / 2.0

    init {
        setDefaultCommand(CmdArmPositionStop(this))
    }

    override fun loop() {
        if(armMoving) {
            wobbleArmMotor.power = armController.calculate(wobbleArmMotor.currentPosition.toDouble())
        }
    }

}