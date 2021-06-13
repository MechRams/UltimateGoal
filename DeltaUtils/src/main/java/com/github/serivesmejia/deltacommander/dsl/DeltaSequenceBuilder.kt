package com.github.serivesmejia.deltacommander.dsl

import com.github.serivesmejia.deltacommander.DeltaCommand
import com.github.serivesmejia.deltacommander.command.DeltaSequentialCmd

class DeltaSequenceBuilder(private val block: DeltaSequenceBuilder.() -> Unit) {

    private val commands = mutableListOf<DeltaCommand>()

    operator fun <T : DeltaCommand> T.invoke(): T {
        commands.add(this)
        return this
    }

    fun build(): DeltaSequentialCmd {
        block()
        return DeltaSequentialCmd(*commands.toTypedArray())
    }

}

fun deltaSequence(block: DeltaSequenceBuilder.() -> Unit) = DeltaSequenceBuilder(block).build()