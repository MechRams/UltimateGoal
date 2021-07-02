package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltacommander.reset
import com.github.serivesmejia.deltadrive.hardware.DeltaHardwareHolonomic
import com.github.serivesmejia.deltaevent.gamepad.SuperGamepad
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.commander.subsystem.*
import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.vision.Vision

abstract class MechOpMode(
    val opModeType: OpModeType,
    private val onlyChassis: Boolean = false,
    private val usingIMU: Boolean = true
) : LinearOpMode() {

    val hdw = Hardware(onlyChassis)
    val deltaHdw by lazy { DeltaHardwareHolonomic(hardwareMap) }

    val drive get() = driveSub.drive

    lateinit var intakeConveySub: IntakeConveySubsystem
    lateinit var wobbleArmSub: WobbleArmSubsystem
    lateinit var wobbleArmClawSubsystem: WobbleArmClawSubsystem
    lateinit var shooterSub: ShooterSubsystem
    lateinit var shooterFlickerSub: ShooterFlickerSubsystem
    lateinit var driveSub: MecanumDriveSubsystem

    //crear el objeto vision por si se quiere usar en el autonomo
    val vision by lazy { Vision(hardwareMap) }

    //crear dos "super gamepads" con los gamepads originales
    val superGamepad1 by lazy { SuperGamepad(gamepad1) }
    val superGamepad2 by lazy { SuperGamepad(gamepad2)  }

    override fun runOpMode() {
        Constants.lastOpMode = opModeType

        telemetry = MultipleTelemetry(FtcDashboard.getInstance().telemetry, telemetry)

        //reinicializar el scheduler
        deltaScheduler.reset()

        hdw.initHardware(hardwareMap) //inicializamos el hardware

        // inicializar el delta hardware
        deltaHdw.initHardware(
            hdw.wheelFrontLeft,
            hdw.wheelFrontRight,
            hdw.wheelBackLeft,
            hdw.wheelBackRight, true
        )

        // crear subsistemas
        driveSub = MecanumDriveSubsystem(deltaHdw, telemetry, usingIMU)

        // si queremos utilizar tambien los demas que no sean el drive...
        if(!onlyChassis) {
            intakeConveySub = IntakeConveySubsystem(hdw.motorIntakeConvey)

            wobbleArmSub = WobbleArmSubsystem(
                hdw.motorWobbleArm,
                opModeType == OpModeType.AUTO
            )
            wobbleArmClawSubsystem = WobbleArmClawSubsystem(hdw.servoWobbleClaw)

            shooterSub = ShooterSubsystem(hdw.motorShooterLeft, hdw.motorShooterRight)
            shooterFlickerSub = ShooterFlickerSubsystem(hdw.servoShooterFlicker)

            //ArmPositionResetCmd().schedule(false)
        }

        run()
    }

    abstract fun run()

}

enum class OpModeType {
    AUTO, TELEOP
}