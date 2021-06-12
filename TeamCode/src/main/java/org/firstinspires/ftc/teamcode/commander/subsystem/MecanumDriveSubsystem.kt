package org.firstinspires.ftc.teamcode.commander.subsystem

import com.acmerobotics.dashboard.config.Config
import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.github.serivesmejia.deltacontrol.PIDFCoefficients
import com.github.serivesmejia.deltadrive.drivebase.DeltaMecanumDrive
import com.github.serivesmejia.deltadrive.hardware.DeltaHardwareHolonomic
import com.github.serivesmejia.deltadrive.parameters.IMUDriveParameters
import org.firstinspires.ftc.teamcode.commander.command.drive.DriveStopCmd

class MecanumDriveSubsystem(deltaHdw: DeltaHardwareHolonomic,
                            usingIMU: Boolean = true) : DeltaSubsystem() {

    @Config
    companion object {
        @JvmField var rotatePIDF = com.qualcomm.robotcore.hardware.PIDFCoefficients(0.039, 0.0005, 0.005, 0.0028)
    }

    val drive = DeltaMecanumDrive(deltaHdw)

    init {
        defaultCommand = DriveStopCmd()

        if(usingIMU) {
            drive.initIMU(IMUDriveParameters().apply {
                TASK_COMMAND_REQUIREMENTS = arrayOf(this@MecanumDriveSubsystem)
                COEFFICIENTS = PIDFCoefficients(rotatePIDF.p, rotatePIDF.i, rotatePIDF.d, rotatePIDF.f)
                ERROR_TOLERANCE = 0.5
            })
        }
    }

    override fun loop() { }

}