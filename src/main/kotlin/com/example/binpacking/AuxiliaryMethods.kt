package com.example.binpacking

import com.example.binpacking.entity.Item
import com.example.binpacking.entity.Item.Pivot as Pivot

fun intersect(currentItem: Item, newItem: Item, pivot: Pivot): Boolean {
    val currentItemDimension = currentItem.getDimension()
    val newItemDimension = newItem.getDimension()
    val currentCenterX = currentItem.position.x + currentItemDimension.width / 2
    val currentCenterY = currentItem.position.y + currentItemDimension.depth / 2
    val currentCenterZ = currentItem.position.z + currentItemDimension.height / 2
    val newCenterX = pivot.x + newItemDimension.width / 2
    val newCenterY = pivot.y + newItemDimension.depth / 2
    val newCenterZ = pivot.z + newItemDimension.height / 2

    val differenceX = kotlin.math.max(currentCenterX, newCenterX) - kotlin.math.min(currentCenterX, newCenterX)
    val differenceY = kotlin.math.max(currentCenterY, newCenterY) - kotlin.math.min(currentCenterY, newCenterY)
    val differenceZ = kotlin.math.max(currentCenterZ, newCenterZ) - kotlin.math.min(currentCenterZ, newCenterZ)

    val xIntersect = differenceX < (currentItemDimension.width + newItemDimension.width) / 2
    val yIntersect = differenceY < (currentItemDimension.depth + newItemDimension.depth) / 2
    val zIntersect = differenceZ < (currentItemDimension.height + newItemDimension.height) / 2

    return xIntersect && yIntersect && zIntersect

}

fun setToDecimal(value: Double, numberOfDecimals: Int): Double {
    val formatted: String = String.format("%.${numberOfDecimals.toString()}f", value)
    return formatted.toDouble()
}
