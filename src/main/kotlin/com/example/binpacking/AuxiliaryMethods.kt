package com.example.binpacking

import com.example.binpacking.entity.Axis
import com.example.binpacking.entity.Item
import java.math.BigDecimal
import java.math.RoundingMode

fun rectIntersect(firstItem: Item, secondItem: Item, x: Int, y: Int): Boolean {
    val firstDimension = firstItem.getDimension()
    val secondDimension = secondItem.getDimension()

    val firstCurrentX = firstItem.position[x] + firstDimension[x] / 2
    val firstCurrentY = firstItem.position[y] + firstDimension[y] / 2
    val secondCurrentX = secondItem.position[x] + secondDimension[x] / 2
    val secondCurrentY = secondItem.position[y] + secondDimension[y] / 2

    val differenceX = kotlin.math.max(firstCurrentX, secondCurrentX) - kotlin.math.min(firstCurrentX, secondCurrentX)
    val differenceY = kotlin.math.max(firstCurrentY, secondCurrentY) - kotlin.math.min(firstCurrentY, secondCurrentY)

    return differenceX < (firstDimension[x] + secondDimension[x]) / 2 && differenceY < (firstDimension[y] + secondDimension[y]) / 2
}

fun intersect(firstItem: Item, secondItem: Item): Boolean {
    return (
            rectIntersect(firstItem, secondItem, Axis.WIDTH, Axis.HEIGHT) &&
                    rectIntersect(firstItem, secondItem, Axis.HEIGHT, Axis.DEPTH) &&
                    rectIntersect(firstItem, secondItem, Axis.WIDTH, Axis.DEPTH)
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
