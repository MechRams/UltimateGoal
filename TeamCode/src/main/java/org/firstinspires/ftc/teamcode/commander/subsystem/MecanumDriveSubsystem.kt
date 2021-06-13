package org.firstinspires.ftc.teamcode.commander.subsystem

import com.github.serivesmejia.deltacommander.DeltaSubsystem
import com.github.serivesmejia.deltadrive.drivebase.DeltaMecanumDrive
import com.github.serivesmejia.deltadrive.hardware.DeltaHardwareHolonomic
import com.github.serivesmejia.deltadrive.motors.andymark.NeveRest_Orbital_20
import com.github.serivesmejia.deltadrive.parameters.EncoderDriveParameters
import com.github.serivesmejia.deltadrive.parameters.IMUDriveParameters
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.commander.command.drive.DriveStopCmd

class MecanumDriveSubsystem(deltaHdw: DeltaHardwareHolonomic,
                            telemetry: Telemetry,
                            usingIMU: Boolean = true) : DeltaSubsystem() {

    val drive = DeltaMecanumDrive(deltaHdw)

    init {
        defaultCommand = DriveStopCmd()

        drive.initEncoders(EncoderDriveParameters().apply {
            TASK_COMMAND_REQUIREMENTS = arrayOf(this@MecanumDriveSubsystem)

            TICKS_PER_REV = NeveRest_Orbital_20.TICKS_PER_REVOLUTION
            WHEEL_DIAMETER_INCHES = 4.0
        })

        if(usingIMU) {
            telemetry.addData("[>]", "Inicializando IMU, espera...")
            telemetry.update()

            drive.initIMU(IMUDriveParameters().apply {
                TASK_COMMAND_REQUIREMENTS = arrayOf(this@MecanumDriveSubsystem)
                COEFFICIENTS = Constants.rotatePIDFCoefficients

                ERROR_TOLERANCE = 0.5
            })

            telemetry.update()
        }
    }

    override fun loop() { }

}