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
            .transitionTimed(0.8)
            .onEnter {
                shooterSub.flickerServo.position = ShooterFlickerSubsystem.FLICKER_IN_POS
            }

            .state(State.OUT)
            .transitionTimed(0.8)
            .onEnter {
                shooterSub.flickerServo.position = ShooterFlickerSubsystem.FLICKER_OUT_POS
            }

            .state(State.FINISHED)
            .transitionTimed(0.8)

            .exit(State.IN)

            .build()

        stateMachine.looping = true
        stateMachine.start()
    }

    override fun run() {
        stateMachine.update()

        if(!isInfinite && stateMachine.getState() == State.FINISHED)
            requestFinish()
    }

    override fun ending() {
        stateMachine.looping = false
    }

    override fun endCondition() = stateMachine.getState() == State.FINISHED

}

class ShooterFlickOutCmd : DeltaCommand() {

    val shooterSub = require<ShooterFlickerSubsystem>()

    override fun run() {
        shooterSub.flickerServo.position = ShooterFlickerSubsystem.FLICKER_OUT_POS
    }

}