package com.example.binpacking

import java.math.BigDecimal
import java.math.RoundingMode

fun rectIntersect(item1: Item, item2: Item, x: Int, y: Int): Boolean {
    val d1 = item1.getDimension()
    val d2 = item2.getDimension()

    val cx1 = item1.position[x] + d1[x] / 2
    val cy1 = item1.position[y] + d1[y] / 2
    val cx2 = item2.position[x] + d2[x] / 2
    val cy2 = item2.position[y] + d2[y] / 2

    val ix = kotlin.math.max(cx1, cx2) - kotlin.math.min(cx1, cx2)
    val iy = kotlin.math.max(cy1, cy2) - kotlin.math.min(cy1, cy2)

    return ix < (d1[x] + d2[x]) / 2 && iy < (d1[y] + d2[y]) / 2
}

fun intersect(item1: Item, item2: Item): Boolean {
    return (
            rectIntersect(item1, item2, Axis.WIDTH, Axis.HEIGHT) &&
                    rectIntersect(item1, item2, Axis.HEIGHT, Axis.DEPTH) &&
                    rectIntersect(item1, item2, Axis.WIDTH, Axis.DEPTH)
            )
}

fun getLimitNumberOfDecimals(numberOfDecimals: Int): Int {
    return BigDecimal("1.${"0".repeat(numberOfDecimals)}").toInt()
}

fun setToDecimal(value: Double, numberOfDecimals: Int): Double {
    val limitDecimal = getLimitNumberOfDecimals(numberOfDecimals)
    val roundingValue = BigDecimal(value).setScale(limitDecimal - 1, RoundingMode.HALF_EVEN)

    return roundingValue.toDouble()
}
