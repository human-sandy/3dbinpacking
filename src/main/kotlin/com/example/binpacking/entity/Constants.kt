package com.example.binpacking.entity

const val DEFAULT_NUMBER_OF_DECIMALS: Int = 2

enum class RotationType {
    DEPTH_IS_LONGER_THAN_WIDTH, WIDTH_IS_LONGER_THAN_DEPTH
}

class ToteSpec(
    margin: Double = 0.0, // 0.0 ~ 1.0, ex) 0.8
) {
    val width: Double = 385 * (1 - margin)
    val height: Double = 200 * (1 - margin)
    val depth: Double = 480 * (1 - margin)
    val weight: Double = 12000 * (1 - margin)
}
