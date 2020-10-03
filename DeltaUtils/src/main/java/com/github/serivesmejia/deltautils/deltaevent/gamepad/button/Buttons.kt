/*
 * Copyright (c) 2020 FTC Delta Robotics #9351 - Sebastian Erives
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.serivesmejia.deltautils.deltaevent.gamepad.button

import java.util.*

class Buttons {

    private var buttons = HashMap<Button, Int>()
    private var type: Type = Type.UNKNOWN

    enum class Type {
        BUTTONS_PRESSED, BUTTONS_RELEASED, BUTTONS_BEING_PRESSED, UNKNOWN
    }

    constructor (buttons: HashMap<Button, Int>, type: Type) {
        this.buttons = buttons
        this.type = type
    }

    fun `is`(btt: Button): Boolean = buttons.containsKey(btt)

    fun ticks(btt: Button): Int {

        return if (buttons.containsKey(btt)) {
            buttons[btt]!!
        } else {
            0
        }

    }

    fun type(): Type {
        return type
    }

}