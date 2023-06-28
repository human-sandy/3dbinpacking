package com.example.binpacking

const val DEFAULT_NUMBER_OF_DECIMALS: Int = 0
val START_POSITION = mutableListOf<Int>(0, 0, 0)

object RotationType {
    const val WHD = 0
    const val HWD = 1
    const val HDW = 2
    const val DHW = 3
    const val DWH = 4
    const val WDH = 5

    val ALL = listOf(WHD, HWD, HDW, DHW, DWH, WDH)
}

object Axis {
    const val WIDTH = 0
    const val HEIGHT = 1
    const val DEPTH = 2

    val all = listOf(WIDTH, HEIGHT, DEPTH)
}

object ToteSpec {
    const val WIDTH: Int = 360
    const val HEIGHT: Int = 460
    const val DEPTH: Int = 130
    const val WEIGHT: Int = 12000
}