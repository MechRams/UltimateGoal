package org.firstinspires.ftc.teamcode.commander.command.shooter

import com.github.serivesmejia.deltacommander.DeltaCommand
import com.noahbres.jotai.StateMachine
import com.noahbres.jotai.StateMachineBuilder
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterFlickerSubsystem

class ShooterFlickCmd(val isInfinite: Boolean = true) : DeltaCommand() {

    val shooterSub = require<ShooterFlickerSubsystem>()

    enum class State { IN, OUT, FINISHED }

    private lateinit var stateMachine: StateMachine<State>

    override fun init() {
        stateMachine = StateMachineBuilder<State>()
            .state(State.IN)
            .onEnter {
                shooterSub.flickerServo.position = ShooterFlickerSubsystem.FLICKER_IN_POS
            }

            .transitionTimed(0.8)
            .state(State.OUT)
            .onEnter {
                shooterSub.flickerServo.position = ShooterFlickerSubsystem.FLICKER_OUT_POS
            }

            .transitionTimed(0.8)
            .state(State.FINISHED)

            .build()

        stateMachine.looping = isInfinite
        stateMachine.start()
    }

    override fun run() {
        stateMachine.update()

        if(!stateMachine.running)
            requestFinish()
    }

    override fun ending() {
        stateMachine.looping = false
    }

    override fun endCondition() = !stateMachine.running

}

class ShooterFlickOutCmd : DeltaCommand() {

    val shooterSub = require<ShooterFlickerSubsystem>()

    override fun run() {
        shooterSub.flickerServo.position = ShooterFlickerSubsystem.FLICKER_OUT_POS
    }

}