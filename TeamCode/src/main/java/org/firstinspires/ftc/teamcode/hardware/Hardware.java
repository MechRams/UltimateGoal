package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Hardware {

    //hardwaremap que se obtuvo en el constructor
    public HardwareMap hwMap;

    //llantas
    public DcMotor wheelFrontRight = null;
    public DcMotor wheelFrontLeft = null;
    public DcMotor wheelBackRight = null;
    public DcMotor wheelBackLeft = null;

    public boolean useSleeps = true;

    //sensores
    //public ColorSensor colorSensor = null; (ejemplo)

    //si el boolean "invertedChassis" es true, las llantas de atras del chasis se convierten en las de adelante y viceversa.
    public void initHardware(HardwareMap hdwMap, boolean invertedChassis){

        hwMap = hdwMap;

        //se obtienen todos los motores, servos y sensores del hardwaremap dado
        if(invertedChassis) {
            wheelFrontRight = hwMap.get(DcMotor.class, "BL");
            wheelFrontLeft = hwMap.get(DcMotor.class, "BR");
            wheelBackRight = hwMap.get(DcMotor.class, "FL");
            wheelBackLeft = hwMap.get(DcMotor.class, "FR");
        }else{
            wheelFrontRight = hwMap.get(DcMotor.class, "FR");
            wheelFrontLeft = hwMap.get(DcMotor.class, "FL");
            wheelBackRight = hwMap.get(DcMotor.class, "BR");
            wheelBackLeft = hwMap.get(DcMotor.class, "BL");
        }

        //La direccion de estos motores sera FORWARD

        //La direccion de estos motores sera REVERSE

        //el power de todos los motores se define a 0
        wheelFrontRight.setPower(0);
        wheelBackRight.setPower(0);
        wheelFrontLeft.setPower(0);
        wheelBackLeft.setPower(0);

        //estos motores frenaran si su power es 0

        //se define la posicion por default de estos servos

        //definimos los motores que correran con y sin encoders
        wheelFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //declaramos todos los motores que tienen encoders conectados. generalmente solo son las llantas, pero a veces tambien sera util tener un lift o algo similar con encoders, para poder hacer que vaya a posiciones especificas.
        wheelFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //esta es la parte en la que se declaran metodos publicos para poder definir la posicion de los servos en un solo lugar.
    //esto lo hice en Skystone para no tener que cambiar las posiciones de los servos en cada uno de los autonomos cada que cambiabamos o agregabamos algo
    //recomiendo llamar a estos metodos con nombres explicativos (en ingles) tales como armDown(), armUp(), grabCube(), releaseCube(), etc.

    public final void sleep(long milliseconds) { //el metodo que congela el programa.

        if(!useSleeps) return;

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

}

//el vacio