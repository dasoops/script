package com.dasoops.script

import java.awt.MouseInfo
import java.awt.Point
import java.awt.Robot
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage

fun Robot.keyClick(code: Int) {
    keyHold(code, 0)
}

fun Robot.keyHold(code: Int, delay: Int) {
    keyHold(code) {
        delay(delay)
    }
}

fun Robot.keyHold(code: Int, func: () -> Unit) {
    keyPress(code)
    func.invoke()
    keyRelease(code)
}

val Point.axis get() = Axis(x, y)


fun Robot.translate(x: Int, y: Int) {
    MouseInfo.getPointerInfo().location.run {
        translate(x, y)
        mouseMove(axis)
    }
}

fun Robot.mouseMove(axis: Axis) {
    mouseMove(axis.x, axis.y)
}

fun Robot.mouseClick() {
    mousePress(MouseEvent.BUTTON1_DOWN_MASK)
    mouseRelease(MouseEvent.BUTTON1_DOWN_MASK)
}

fun Robot.mouseRightClick() {
    mousePress(MouseEvent.BUTTON3_DOWN_MASK)
    mouseRelease(MouseEvent.BUTTON3_DOWN_MASK)
}

fun Robot.mouseHold(code: Int, func: () -> Unit) {
    mousePress(code)
    func.invoke()
    mouseRelease(code)
}

fun Robot.mouseHoldRight(func: () -> Unit) {
    mouseHold(MouseEvent.BUTTON3_DOWN_MASK, func)
}

fun BufferedImage.rgb(axis: Axis) = getRGB(axis.x, axis.y)
