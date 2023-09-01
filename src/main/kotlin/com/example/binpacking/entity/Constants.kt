package com.example.binpacking.entity

const val DEFAULT_NUMBER_OF_DECIMALS: Int = 0

object RotationType{
    const val LONGER_W = 0
    const val LONGER_D = 1
}

class ToteSpec(
    margin: Double = 0.0, // 0.0 ~ 1.0, ex) 0.8 #
) {
    val width: Double = 385 * (1 - margin)
    val height: Double = 200 * (1 - margin)
    val depth: Double = 480 * (1 - margin)
    val weight: Double = 12000 * (1 - margin)
}
