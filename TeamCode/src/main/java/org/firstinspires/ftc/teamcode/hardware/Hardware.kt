package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

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

    var useSleeps = true

    //sensores
    //public ColorSensor colorSensor = null; (ejemplo)
    //si el boolean "invertedChassis" es true, las llantas de atras del chasis se convierten en las de adelante y viceversa.
    fun initHardware(hdwMap: HardwareMap, invertedChassis: Boolean) {

        this.hdwMap = hdwMap

        //se obtienen todos los motores, servos y sensores del hardwaremap dado
        if (invertedChassis) {
            wheelFrontRight = hdwMap.get(DcMotor::class.java, "BL")
            wheelFrontLeft = hdwMap.get(DcMotor::class.java, "BR")
            wheelBackRight = hdwMap.get(DcMotor::class.java, "FL")
            wheelBackLeft = hdwMap.get(DcMotor::class.java, "FR")
        } else {
            wheelFrontRight = hdwMap.get(DcMotor::class.java, "FR")
            wheelFrontLeft = hdwMap.get(DcMotor::class.java, "FL")
            wheelBackRight = hdwMap.get(DcMotor::class.java, "BR")
            wheelBackLeft = hdwMap.get(DcMotor::class.java, "BL")
        }

        motorWobbleArm = hdwMap.get(DcMotor::class.java, "WA")

        //La direccion de estos motores sera FORWARD
        motorWobbleArm.direction = DcMotorSimple.Direction.FORWARD

        //La direccion de estos motores sera REVERSE

        //el power de todos los motores se define a 0
        wheelFrontRight.power = 0.0
        wheelBackRight.power = 0.0
        wheelFrontLeft.power = 0.0
        wheelBackLeft.power = 0.0
        motorWobbleArm.power = 0.0

        //estos motores frenaran si su power es 0
        motorWobbleArm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        //se define la posicion por default de estos servos

        //definimos los motores que correran con y sin encoders
        wheelFrontRight.mode = DcMotor.RunMode.RUN_USING_ENCODER //declaramos todos los motores que tienen encoders conectados. generalmente solo son las llantas, pero a veces tambien sera util tener un lift o algo similar con encoders, para poder hacer que vaya a posiciones especificas.
        wheelFrontLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelBackRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
        wheelBackLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motorWobbleArm.mode = DcMotor.RunMode.RUN_USING_ENCODER

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

}