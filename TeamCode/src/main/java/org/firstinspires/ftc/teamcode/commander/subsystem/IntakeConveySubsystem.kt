package org.firstinspires.ftc.teamcode.commander.subsystem

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.CmdIntakeConveyStop

class IntakeConveySubsystem(val intakeConveyMotor: DcMotor) : DeltaSubsystem() {

    init {
        //defaultCommand = CmdIntakeConveyStop()
    }

    override fun loop() {
        intakeConveyMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

}