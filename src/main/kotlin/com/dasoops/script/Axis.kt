package com.dasoops.script

data class Axis(
    val x: Int,
    val y: Int,
)

data class ColorAxis(
    val x: Int,
    val y: Int,
    val rgb: Int
) {
    constructor(axis: Axis, rgb: Int) : this(axis.x, axis.y, rgb)
}