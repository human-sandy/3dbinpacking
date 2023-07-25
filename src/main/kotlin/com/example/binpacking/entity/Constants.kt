package com.example.binpacking.entity

const val DEFAULT_NUMBER_OF_DECIMALS: Int = 0
val START_POSITION = listOf<Int>(0, 0, 0)

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

    val ALL = listOf(WIDTH, HEIGHT, DEPTH)
}

class ToteSpec(
    margin: Double, // 0.0 ~ 1.0, ex) 0.8
) {
    val width: Double = 385 * (1 - margin)
    val height: Double = 200 * (1 - margin)
    val depth: Double = 480 * (1 - margin)
    val weight: Double = 12000 * (1 - margin)
}
