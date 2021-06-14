package com.github.serivesmejia.deltasimple

import com.qualcomm.robotcore.hardware.HardwareMap

abstract class SimpleHardware {

    lateinit var hdwMap: HardwareMap

    open fun initHardware(hardwareMap: HardwareMap) {
        hdwMap = hardwareMap
    }

    inline fun <reified T> hardware(name: String) = lazy { device<T>(name) }

    inline fun <reified T> device(name: String) = hdwMap.get(T::class.java, name)!!

}