package org.firstinspires.ftc.teamcode.autonomous

import com.github.serivesmejia.deltacommander.deltaScheduler
import com.noahbres.jotai.StateMachineBuilder
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.MechOpMode
import org.firstinspires.ftc.teamcode.OpModeType
import org.firstinspires.ftc.teamcode.commander.command.shooter.ShooterRunCmd

@TeleOp(name="Shooter Tuning", group="Test")
class AutonomoShooterTuning : MechOpMode(OpModeType.AUTO) {

    enum class State { RAMPING_UP, RAMPING_DOWN, FULL_POWER, MEDIUM_POWER, STOP, LOW_POWER }

    override fun run() {
        deltaHdw.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        var shooterPower = 1.0

        var targetVelo = 0.0
        val maxVelo = shooterSub.maxTps

        val stateMachine = StateMachineBuilder<State>()
            .state(State.RAMPING_UP)
            .transitionTimed(8.0)
            .onEnter { shooterPower = 0.0 }
            .loop {
                if (shooterPower <= 0.5)
                    shooterPower += 0.005
            }

            .state(State.RAMPING_DOWN)
            .transitionTimed(8.0)
            .loop {
                if (shooterPower > 0)
                    shooterPower -= 0.005
            }

            .state(State.FULL_POWER)
            .transitionTimed(8.0)
            .onEnter { shooterPower = 0.5 }

            .state(State.MEDIUM_POWER)
            .transitionTimed(8.0)
            .onEnter { shooterPower = 0.3 }

            .state(State.STOP)
            .transitionTimed(8.0)
            .onEnter { shooterPower = 0.0 }

            .state(State.LOW_POWER)
            .transitionTimed(8.0)
            .onEnter { shooterPower = 0.1 }

            .exit(State.RAMPING_UP)

            .build()

        stateMachine.looping = true

        + ShooterRunCmd({ targetVelo }, true)

        waitForStart()

        stateMachine.start()

        while (opModeIsActive()) {
            deltaHdw.clearBulkCache()

            stateMachine.update()

            targetVelo = maxVelo * shooterPower * 0.8

            deltaScheduler.update()

            telemetry.addData("bottom limit", "0")
            telemetry.addData("bottom upper limit", maxVelo)
            telemetry.addData("state", stateMachine.getState().name)
            telemetry.addData("power", shooterPower)
            telemetry.addData("left velo", shooterSub.leftMotor.velocity)
            telemetry.addData("right velo", shooterSub.rightMotor.velocity)
            telemetry.addData("target velo", targetVelo)
            telemetry.addData("power", shooterPower)
            telemetry.update()
        }
    }

}