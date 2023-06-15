package com.example.binpacking

const val DEFAULT_NUMBER_OF_DECIMALS: Int = 0
val START_POSITION = mutableListOf<Int>(0, 0, 0)

class RotationType() {
    val RT_WHD = 0
    val RT_HWD = 1
    val RT_HDW = 2
    val RT_DHW = 3
    val RT_DWH = 4
    val RT_WDH = 5

    val ALL = listOf(RT_WHD, RT_HWD, RT_HDW, RT_DHW, RT_DWH, RT_WDH)
}

class Axis() {
    val WIDTH = 0
    val HEIGHT = 1
    val DEPTH = 2

    val ALL = listOf(WIDTH, HEIGHT, DEPTH)
}

class ToteSpec() {
    var width: Int = 360
    var height: Int = 460
    var depth: Int = 130
    var weight: Int = 12000
}