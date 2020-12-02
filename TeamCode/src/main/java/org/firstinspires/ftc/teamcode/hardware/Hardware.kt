package org.firstinspires.ftc.teamcode.hardware

import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Hardware {

    //hardwaremap que se obtuvo en el constructor
    lateinit var hdwMap: HardwareMap

    //llantas
    lateinit var wheelFrontRight: DcMotor
    lateinit var wheelFrontLeft: DcMotor
    lateinit var wheelBackRight: DcMotor
    lateinit var wheelBackLeft: DcMotor

    //otros motores
    lateinit var motorWobbleArm: DcMotor

    lateinit var motorShooterLeft: MotorEx
    lateinit var motorShooterRight: MotorEx

    //servos
    lateinit var servoWobbleClaw: Servo

    //sensores
    //public ColorSensor colorSensor = null; (ejemplo)

    fun initHardware(hdwMap: HardwareMap) {

        this.hdwMap = hdwMap

        //se obtienen todos los motores, servos y sensores del hardwaremap dado
        wheelFrontRight = hdwMap.getDevice<DcMotor>("FR")!!
        wheelFrontLeft = hdwMap.getDevice<DcMotor>("FL")!!
        wheelBackRight = hdwMap.getDevice<DcMotor>("BL")!!
        wheelBackLeft = hdwMap.getDevice<DcMotor>("BR")!!

        motorWobbleArm = hdwMap.getDevice<DcMotor>("WA")!!
        motorShooterLeft = MotorEx(hdwMap, "SL")
        motorShooterRight = MotorEx(hdwMap, "SR")

        servoWobbleClaw = hdwMap.getDevice<Servo>("CW")!!

        //La direccion de estos motores sera REVERSE
        motorShooterRight.motorEx.direction = DcMotorSimple.Direction.REVERSE

        //estos motores frenaran si su power es 0
        motorWobbleArm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        //se define la posicion por default de estos servos

        //definimos los motores que correran con y sin encoders
        wheelFrontRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelFrontLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelBackRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelBackLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motorWobbleArm.mode = DcMotor.RunMode.RUN_USING_ENCODER

        val hubs = hdwMap.getAll(LynxModule::class.java)

        for (hub in hubs) {
            hub.bulkCachingMode = LynxModule.BulkCachingMode.AUTO
        }

    }

    inline fun <reified T> HardwareMap.getDevice(name: String): T? = this.get(T::class.java, name)

}