package com.example.binpacking

import java.math.BigDecimal
import java.math.RoundingMode

fun getLimitNumberOfDecimals(numberOfDecimals: Int): Int {
    return BigDecimal("1.${"0".repeat(numberOfDecimals)}").toInt()
}

fun setToDecimal(value: Double, numberOfDecimals: Int): Double {
    val limitDecimal = getLimitNumberOfDecimals(numberOfDecimals)
    val roundingValue = BigDecimal(value).setScale(limitDecimal - 1, RoundingMode.HALF_EVEN)
    // 아이템 정보 반올림해서

    return roundingValue.toDouble()
}
