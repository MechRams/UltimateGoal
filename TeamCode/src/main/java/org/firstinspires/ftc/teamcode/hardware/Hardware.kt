package org.firstinspires.ftc.teamcode.hardware

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.github.serivesmejia.deltasimple.SimpleHardware
import com.qualcomm.robotcore.hardware.*

class Hardware(val onlyDrive: Boolean) : SimpleHardware() {

    //llantas
    val wheelFrontLeft  by hardware<DcMotor>("FL")
    val wheelFrontRight by hardware<DcMotor>("FR")
    val wheelBackLeft   by hardware<DcMotor>("BL")
    val wheelBackRight  by hardware<DcMotor>("BR")

    //otros motores
    val motorWobbleArm by motorEx("WA")

    val motorShooterLeft  by hardware<DcMotorEx>("SL")
    val motorShooterRight by hardware<DcMotorEx>("SR")

    val motorIntakeConvey by hardware<DcMotor>("IN")

    //servos
    val servoShooterFlicker by hardware<Servo>("SF")
    val servoWobbleClaw     by hardware<Servo>("WC")

    //sensores
    //public ColorSensor colorSensor = null; (ejemplo)

    override fun initHardware(hardwareMap: HardwareMap) {
        super.initHardware(hardwareMap)

        if(!onlyDrive) {
            //La direccion de estos motores sera REVERSE
            wheelFrontRight.direction = DcMotorSimple.Direction.REVERSE
            wheelBackRight.direction = DcMotorSimple.Direction.REVERSE

            motorShooterRight.direction = DcMotorSimple.Direction.REVERSE
            motorIntakeConvey.direction = DcMotorSimple.Direction.REVERSE

            //estos motores frenaran si su power es 0
            motorWobbleArm.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        }

        //se define la posicion por default de estos servos
        servoWobbleClaw.position = 0.1

        //definimos los motores que correran con y sin encoders
        wheelFrontRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelFrontLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelBackRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelBackLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER

        if(!onlyDrive) {
            motorWobbleArm.setRunMode(Motor.RunMode.VelocityControl)
        }
    }

    private fun motorEx(name: String) = lazy { MotorEx(hdwMap, name) }

}