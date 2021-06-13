package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.github.serivesmejia.deltacommander.deltaScheduler
import com.github.serivesmejia.deltacommander.reset
import com.github.serivesmejia.deltadrive.hardware.DeltaHardwareHolonomic
import com.github.serivesmejia.deltadrive.utils.Invert
import com.github.serivesmejia.deltaevent.gamepad.SuperGamepad
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.commander.subsystem.*
import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.vision.Vision

abstract class MechOpMode(
    private val onlyChassis: Boolean = false,
    private val usingIMU: Boolean = true
) : LinearOpMode() {

    val hdw = Hardware(onlyChassis)
    lateinit var deltaHdw: DeltaHardwareHolonomic

    val drive get() = driveSub.drive

    lateinit var intakeConveySub: IntakeConveySubsystem
    lateinit var wobbleArmSub: WobbleArmSubsystem
    lateinit var shooterSub: ShooterSubsystem
    lateinit var shooterFlickerSub: ShooterFlickerSubsystem
    lateinit var driveSub: MecanumDriveSubsystem

    lateinit var vision: Vision

    lateinit var superGamepad1: SuperGamepad
    lateinit var superGamepad2: SuperGamepad

    override fun runOpMode() {
        telemetry = MultipleTelemetry(FtcDashboard.getInstance().telemetry, telemetry)

        //reinicializar el scheduler
        deltaScheduler.reset()

        superGamepad1 = SuperGamepad(gamepad1) //crear dos "super gamepads" con los gamepads originals
        superGamepad2 = SuperGamepad(gamepad2)

        //crear el objeto vision por si se quiere usar en el autonomo
        vision = Vision(hardwareMap)

        hdw.initHardware(hardwareMap) //inicializamos el hardware
        deltaHdw = DeltaHardwareHolonomic(hardwareMap, Invert.RIGHT_SIDE)

        // inicializar el delta hardware
        deltaHdw.initHardware(
                hdw.wheelFrontLeft,
                hdw.wheelFrontRight,
                hdw.wheelBackLeft,
                hdw.wheelBackRight,
                true
        )

        // crear subsistemas
        driveSub = MecanumDriveSubsystem(deltaHdw, telemetry, usingIMU)

        // si queremos utilizar tambien los demas que no sean el drive...
        if(!onlyChassis) {
            intakeConveySub = IntakeConveySubsystem(hdw.motorIntakeConvey)
            wobbleArmSub = WobbleArmSubsystem(hdw.motorWobbleArm, hdw.servoWobbleClaw)
            shooterSub = ShooterSubsystem(hdw.motorShooterLeft, hdw.motorShooterRight )
            shooterFlickerSub = ShooterFlickerSubsystem(hdw.servoShooterFlicker)
        }

        run()
    }

    abstract fun run()

}