package org.firstinspires.ftc.teamcode.hardware

import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap


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

    var useSleeps = true

    //sensores
    //public ColorSensor colorSensor = null; (ejemplo)
    //si el boolean "invertedChassis" es true, las llantas de atras del chasis se convierten en las de adelante y viceversa.
    fun initHardware(hdwMap: HardwareMap, invertedChassis: Boolean) {

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

        //La direccion de estos motores sera FORWARD
        motorWobbleArm.direction = DcMotorSimple.Direction.FORWARD

        //La direccion de estos motores sera REVERSE
        motorShooterRight.motorEx.direction = DcMotorSimple.Direction.FORWARD

        //estos motores frenaran si su power es 0
        motorWobbleArm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        //se define la posicion por default de estos servos

        //definimos los motores que correran con y sin encoders
        wheelFrontRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelFrontLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelBackRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelBackLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motorWobbleArm.mode = DcMotor.RunMode.RUN_USING_ENCODER

        val hubs = hardwareMap.getAll(LynxModule::class.java)

        for (hub in hubs) {
            hub.bulkCachingMode = LynxModule.BulkCachingMode.AUTO
        }

    }

    //esta es la parte en la que se declaran metodos publicos para poder definir la posicion de los servos en un solo lugar.
    //esto lo hice en Skystone para no tener que cambiar las posiciones de los servos en cada uno de los autonomos cada que cambiabamos o agregabamos algo
    //recomiendo llamar a estos metodos con nombres explicativos (en ingles) tales como armDown(), armUp(), grabCube(), releaseCube(), etc.

    fun sleep(milliseconds: Long) { //el metodo que congela el programa.

        if (!useSleeps) return

        try {
            Thread.sleep(milliseconds)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }

    }

    inline fun <reified T> HardwareMap.getDevice(name: String): T? = this.get(T::class.java, name)

}