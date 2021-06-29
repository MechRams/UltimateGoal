package com.github.serivesmejia.deltacommander.dsl

import com.github.serivesmejia.deltacommander.DeltaCommand
import com.github.serivesmejia.deltacommander.command.DeltaRunCmd
import com.github.serivesmejia.deltacommander.command.DeltaSequentialCmd

class DeltaSequenceBuilder(private val block: DeltaSequenceBuilder.() -> Unit) {

    private val commands = mutableListOf<DeltaCommand>()

    operator fun <T : DeltaCommand> T.unaryMinus(): T {
        commands.add(this)
        return this
    }

    fun DeltaCommand.dontBlock() = DeltaRunCmd(this::schedule)

    fun build(): DeltaSequentialCmd {
        block()
        return DeltaSequentialCmd(*commands.toTypedArray())
    }

}

fun deltaSequence(block: DeltaSequenceBuilder.() -> Unit) = DeltaSequenceBuilder(block).build()