package com.example.binpacking.entity

import com.example.binpacking.entity.Item.Pivot
import com.example.binpacking.intersect

class Tote(
    val name: String,
    val width: Double,
    val height: Double,
    val depth: Double,
    private val maxWeight: Double,
) {

    private val numberOfDecimals = DEFAULT_NUMBER_OF_DECIMALS

    private val candidatePositions = mutableListOf(Pivot(0.0, 0.0, 0.0))

    var remainVolume = width * height * depth
    val items = mutableListOf<Item>()
    val unfittedItems = mutableListOf<Item>()


    fun putItem(item: Item): Boolean {
        if (getTotalWeight() + item.weight >= maxWeight) return false

        for (pivot in candidatePositions) {
            if (canInsert(item, pivot)) {
                item.position = pivot
                calculateCandidatePositions(item, pivot)
                return true
            }
            item.swapWidthDepth()
            if (canInsert(item, pivot)) {
                item.position = pivot
                calculateCandidatePositions(item, pivot)
                return true
            }
            item.swapWidthDepth()
        }
        return false
    }

    private fun getTotalWeight(): Double {
        return this.items.sumOf { item -> item.weight }
    }

    private fun canInsert(item: Item, pivot: Pivot): Boolean {
        for (currentItem in items) {
            if (intersect(currentItem, item, pivot)) {
                return false
            }
        }
        return width >= pivot.x + item.cbm.width &&
                depth >= pivot.y + item.cbm.depth &&
                height >= pivot.z + item.cbm.height
    }

    private fun calculateCandidatePositions(item: Item, pivot: Pivot) {
        candidatePositions.remove(pivot)
        var newPivot = offsetPivotByItem(pivot, Pivot(item.cbm.width, 0.0, 0.0))
        if (!candidatePositions.contains(newPivot)) {
            candidatePositions.add(newPivot)
        }
        newPivot = offsetPivotByItem(pivot, Pivot(0.0, item.cbm.depth, 0.0))
        if (!candidatePositions.contains(newPivot)) {
            candidatePositions.add(newPivot)
        }
        newPivot = offsetPivotByItem(pivot, Pivot(0.0, 0.0, item.cbm.height))
        if (!candidatePositions.contains(newPivot)) {
            candidatePositions.add(newPivot)
        }
    }

    private fun offsetPivotByItem(initialPose: Pivot, itemLength: Pivot): Pivot {
        return Pivot(initialPose.x + itemLength.x, initialPose.y + itemLength.y, initialPose.z + itemLength.z)
    }

    override fun toString(): String {
        return "Tote(name='$name', width=$width, height=$height, depth=$depth, maxWeight=$maxWeight, items=$items, unfittedItems=$unfittedItems, numberOfDecimals=$numberOfDecimals)"
    }
}
