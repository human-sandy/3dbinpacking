package com.example.binpacking

import com.example.binpacking.entity.Item
import kotlin.math.max
import kotlin.math.min
import com.example.binpacking.entity.Item.Point as Pivot


fun intersect(existingItem: Item, newItem: Item, point: Pivot): Boolean {
    val existingItemDimension = existingItem.getDimension()
    val newItemDimension = newItem.getDimension()

    val existingCenterX = existingItem.position.x + existingItemDimension.width / 2
    val existingCenterY = existingItem.position.y + existingItemDimension.depth / 2
    val existingCenterZ = existingItem.position.z + existingItemDimension.height / 2
    val newCenterX = point.x + newItemDimension.width / 2
    val newCenterY = point.y + newItemDimension.depth / 2
    val newCenterZ = point.z + newItemDimension.height / 2

    val differenceX = max(existingCenterX, newCenterX) - min(existingCenterX, newCenterX)
    val differenceY = max(existingCenterY, newCenterY) - min(existingCenterY, newCenterY)
    val differenceZ = max(existingCenterZ, newCenterZ) - min(existingCenterZ, newCenterZ)

    val xIntersect = differenceX < (existingItemDimension.width + newItemDimension.width) / 2
    val yIntersect = differenceY < (existingItemDimension.depth + newItemDimension.depth) / 2
    val zIntersect = differenceZ < (existingItemDimension.height + newItemDimension.height) / 2

    return xIntersect && yIntersect && zIntersect

}

fun setToDecimal(value: Double, numberOfDecimals: Int): Double {
    val formatted: String = String.format("%.${numberOfDecimals.toString()}f", value)
    return formatted.toDouble()
}
