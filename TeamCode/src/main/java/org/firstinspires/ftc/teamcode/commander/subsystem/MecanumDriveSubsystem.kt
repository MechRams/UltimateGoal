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

    val drive = DeltaMecanumDrive(deltaHdw, telemetry)

    init {
        defaultCommand = DriveStopCmd()

        if(usingIMU) {
            drive.initIMU(IMUDriveParameters().apply {
                TASK_COMMAND_REQUIREMENTS = arrayOf(this@MecanumDriveSubsystem)
                COEFFICIENTS = Constants.rotatePIDFCoefficients

                DEAD_ZONE = 0.1
                ERROR_TOLERANCE = 4.0
            })

            drive.waitForIMUCalibration()

            telemetry.clearAll()
            telemetry.update()
        }

        drive.initEncoders(EncoderDriveParameters().apply {
            TASK_COMMAND_REQUIREMENTS = arrayOf(this@MecanumDriveSubsystem)

            TICKS_PER_REV = NeveRest_Orbital_20.TICKS_PER_REVOLUTION
            WHEEL_DIAMETER_INCHES = 4.0
            SHOW_CURRENT_DISTANCE = true

            DRIVE_STRAIGHT_COEFFICIENTS = Constants.drivePIDFCoefficients
            DRIVE_STRAIGHT_DEADZONE = 0.1
            DRIVE_STRAIGHT_DEGREE_TOLERANCE = 3.0
        })
    }

    override fun loop() { }

}