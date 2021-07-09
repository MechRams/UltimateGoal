package org.firstinspires.ftc.teamcode.commander.command.shooter

import com.github.serivesmejia.deltacommander.DeltaCommand
import com.noahbres.jotai.StateMachine
import com.noahbres.jotai.StateMachineBuilder
import org.firstinspires.ftc.teamcode.Constants
import org.firstinspires.ftc.teamcode.commander.subsystem.ShooterFlickerSubsystem

class ShooterAutoFlick(val flickTimes: Int = 3) : DeltaCommand() {

    val shooterSub = require<ShooterFlickerSubsystem>()

    enum class State { IN, OUT, FINISHED }

    private lateinit var stateMachine: StateMachine<State>

    private var flickCount = 0

    override fun init() {
        stateMachine = StateMachineBuilder<State>()
                .state(State.IN)
                .transitionTimed(0.7)
                .loop {
                    shooterSub.flickerServo.position = Constants.flickerInPos
                }

                .state(State.OUT)
                .transitionTimed(0.8)
                .loop {
                    shooterSub.flickerServo.position = Constants.flickerOutPos
                }

                .state(State.FINISHED)
                .transitionTimed(0.3)
                .onEnter { flickCount++ }

                .exit(State.IN)

                .build()

        stateMachine.looping = true
        stateMachine.start()
    }

    override fun run() {
        stateMachine.update()

        if(flickCount >= flickTimes)
            requestFinish()
    }

    override fun endCondition() = flickCount >= flickTimes

}