package com.example.binpacking.entity

import com.example.binpacking.intersect
import com.example.binpacking.setToDecimal

class Tote(val name: String, var width: Double, var height: Double, var depth: Double, var maxWeight: Double) {
    val items: MutableList<Item> = mutableListOf()
    val unfittedItems: MutableList<Item> = mutableListOf()
    private var numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS

    fun formatNumbers(numberOfDecimals: Int) {
        width = setToDecimal(this.width, numberOfDecimals)
        height = setToDecimal(this.height, numberOfDecimals)
        depth = setToDecimal(this.depth, numberOfDecimals)
        maxWeight = setToDecimal(this.maxWeight, numberOfDecimals)
        this.numberOfDecimals = numberOfDecimals
    }

    private fun getVolume(): Double {
        return setToDecimal(width * height * depth, this.numberOfDecimals)
    }

    private fun getTotalWeight(): Double {
        return this.items.sumOf { item -> item.weight }
    }

    fun putItem(item: Item, pivot: List<Int>): Boolean {
        var fit = false
        val validItemPosition = item.position
        item.position = pivot.toMutableList()

        for (i in 0 until RotationType.ALL.size) {
            item.rotationType = i
            val dimension = item.getDimension()

            if (
                width < pivot[0] + dimension[0] ||
                height < pivot[1] + dimension[1] ||
                depth < pivot[2] + dimension[2]
            )
                continue

            fit = true

            for (currentItemInTote in items) {
                if (intersect(currentItemInTote, item)) {
                    fit = false
                    break
                }
            }

            if (fit) {
                if (getTotalWeight() + item.weight > maxWeight)
                    return false
                items.add(item)
            } else {
                item.position = validItemPosition
            }
            return fit
        }

        item.position = validItemPosition

        return false
    }

    override fun toString(): String {
        return "Tote(name='$name', width=$width, height=$height, depth=$depth, maxWeight=$maxWeight, items=$items, unfittedItems=$unfittedItems, numberOfDecimals=$numberOfDecimals)"
    }

}