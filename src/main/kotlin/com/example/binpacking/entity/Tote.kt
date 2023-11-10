package com.example.binpacking.entity

import com.example.binpacking.entity.Item.Point
import com.example.binpacking.intersect

class Tote(
    val name: String,
    val width: Double,
    val height: Double,
    val depth: Double,
    private val maxWeight: Double,
) {

    private val numberOfDecimals = DEFAULT_NUMBER_OF_DECIMALS

    private val positionCandidates = mutableListOf(Point(0.0, 0.0, 0.0))

    val items = mutableListOf<Item>()
    val unfittedItems = mutableListOf<Item>()
    var remainedVolume = width * height * depth


    fun putItem(item: Item): Boolean {
        if (getTotalWeight() + item.weight >= maxWeight) return false

        for (point in positionCandidates) {
            if (canInsert(item, point)) {
                item.position = point
                generateNewCandidates(item, point)
                return true
            }
            item.swapWidthDepth()
            if (canInsert(item, point)) {
                item.position = point
                generateNewCandidates(item, point)
                return true
            }
            item.swapWidthDepth()
        }
        return false
    }

    private fun getTotalWeight(): Double {
        return this.items.sumOf { item -> item.weight }
    }

    private fun canInsert(newItem: Item, position: Point): Boolean {
        for (existingItem in items) {
            if (intersect(existingItem, newItem, position))
                return false
        }
        return width >= position.x + newItem.cbm.width
                && depth >= position.y + newItem.cbm.depth
                && height >= position.z + newItem.cbm.height
    }

    private fun generateNewCandidates(item: Item, itemPosition: Point) {
        positionCandidates.remove(itemPosition)
        var newCandidate = calculateCandidate(itemPosition, Point(item.cbm.width, 0.0, 0.0))
        if (!positionCandidates.contains(newCandidate)) {
            positionCandidates.add(newCandidate)
        }
        newCandidate = calculateCandidate(itemPosition, Point(0.0, item.cbm.depth, 0.0))
        if (!positionCandidates.contains(newCandidate)) {
            positionCandidates.add(newCandidate)
        }
        newCandidate = calculateCandidate(itemPosition, Point(0.0, 0.0, item.cbm.height))
        if (!positionCandidates.contains(newCandidate)) {
            positionCandidates.add(newCandidate)
        }
    }

    private fun calculateCandidate(initialPosition: Point, itemSideLength: Point): Point {
        return Point(
            initialPosition.x + itemSideLength.x,
            initialPosition.y + itemSideLength.y,
            initialPosition.z + itemSideLength.z
        )
    }

    override fun toString(): String {
        return "Tote(name='$name', width=$width, height=$height, depth=$depth, maxWeight=$maxWeight, items=$items, unfittedItems=$unfittedItems, numberOfDecimals=$numberOfDecimals)"
    }

}