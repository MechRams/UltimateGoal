package org.firstinspires.ftc.teamcode.commander.subsystem

import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.commander.command.intakeconvey.IntakeConveyStopCmd

class IntakeConveySubsystem(val intakeConveyMotor: DcMotor) : DeltaSubsystem() {

    init {
        defaultCommand = IntakeConveyStopCmd()
    }

    override fun loop() { }

}