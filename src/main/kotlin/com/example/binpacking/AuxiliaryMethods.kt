package com.example.binpacking

import com.example.binpacking.entity.Item
import java.math.BigDecimal
import java.math.RoundingMode

fun intersect(firstItem: Item, secondItem: Item, pivot:List<Double>): Boolean {
    val firstDimension = firstItem.getDimension()
    val secondDimension = secondItem.getDimension()

    val firstCurrentX = firstItem.position[0] + firstDimension[0] / 2
    val firstCurrentY = firstItem.position[1] + firstDimension[1] / 2
    val firstCurrentZ = firstItem.position[2] + firstDimension[2] / 2
    val secondCurrentX = pivot[0] + secondDimension[0] / 2
    val secondCurrentY = pivot[1] + secondDimension[1] / 2
    val secondCurrentZ = pivot[2] + secondDimension[2] / 2

    val differenceX = kotlin.math.max(firstCurrentX, secondCurrentX) - kotlin.math.min(firstCurrentX, secondCurrentX)
    val differenceY = kotlin.math.max(firstCurrentY, secondCurrentY) - kotlin.math.min(firstCurrentY, secondCurrentY)
    val differenceZ = kotlin.math.max(firstCurrentZ, secondCurrentZ) - kotlin.math.min(firstCurrentZ, secondCurrentZ)

    val x = differenceX < (firstDimension[0] + secondDimension[0])/2
    val y = differenceY < (firstDimension[1] + secondDimension[1])/2
    val z = differenceZ < (firstDimension[2] + secondDimension[2])/2

    val bool = x && y && z

    return bool

} // 모든 차원의 지점이 교차하는지 확인해서 두 물건이 교차하는지 여부 반환

fun getLimitNumberOfDecimals(numberOfDecimals: Int): Int {
    return BigDecimal("1.${"0".repeat(numberOfDecimals)}").toInt()
}

fun setToDecimal(value: Double, numberOfDecimals: Int): Double {
    val limitDecimal = getLimitNumberOfDecimals(numberOfDecimals)
    val roundingValue = BigDecimal(value).setScale(limitDecimal - 1, RoundingMode.HALF_EVEN)
    // 아이템 정보 반올림해서

    return roundingValue.toDouble()
}
