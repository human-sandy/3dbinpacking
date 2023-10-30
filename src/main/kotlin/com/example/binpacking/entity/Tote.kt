package com.example.binpacking.entity

import com.example.binpacking.intersect
import com.example.binpacking.entity.Item.Pivot as Pivot

class Tote(
    val name: String,
    val width: Double,
    val height: Double,
    val depth: Double,
    private val maxWeight: Double)
{
    val items: MutableList<Item> = mutableListOf()
    val unfittedItems: MutableList<Item> = mutableListOf()
    private val numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS
    private val pivots: MutableList<Pivot> = mutableListOf(Pivot(0.0, 0.0, 0.0))
    var availSpace = width * height * depth

    private fun getTotalWeight(): Double {
        return this.items.sumOf { item -> item.weight }
    }

    private fun checkSize(item:Item, pivot: Pivot): Boolean {
        var fit = false
        for (currentItem in items){
            if(!intersect(currentItem, item, pivot)){
                continue }
            else {
                return fit }
        }
        if (
            width >= pivot.x + item.cbm.width &&
            depth >= pivot.y + item.cbm.depth &&
            height >= pivot.z + item.cbm.height
        )
        fit = true
        return fit
    }

    private fun sumPoints(pivot: Pivot, gap:Pivot): Pivot {
        return Pivot(pivot.x + gap.x, pivot.y + gap.y, pivot.z + gap.z)
    }

    private fun addPivots(item:Item, pivot:Pivot) {
        pivots.remove(pivot)
        var newPivot = sumPoints(pivot, Pivot(item.cbm.width, 0.0, 0.0))
        if (!pivots.contains(newPivot)){
            pivots.add(newPivot)
        }
        newPivot = sumPoints(pivot, Pivot(0.0, item.cbm.depth, 0.0))
        if (!pivots.contains(newPivot)){
            pivots.add(newPivot)
        }
        newPivot = sumPoints(pivot, Pivot(0.0, 0.0, item.cbm.height))
        if (!pivots.contains(newPivot)){
            pivots.add(newPivot)
        }
    }

    fun putItem(item: Item): Boolean {
        var fit: Boolean = false
        if (getTotalWeight() + item.weight < maxWeight){
            for (pivot in pivots){
                if (checkSize(item, pivot)){
                    fit = true
                    item.position = pivot
                    addPivots(item, pivot)
                    break
                }
                else{ item.widthDepthSwitch()
                    if (checkSize(item, pivot)){
                        fit = true
                        item.position = pivot
                        addPivots(item, pivot)
                        break }
                    else{
                        item.widthDepthSwitch()
                    }
                }
            }
        }
        return fit
    }

    override fun toString(): String {
        return "Tote(name='$name', width=$width, height=$height, depth=$depth, maxWeight=$maxWeight, items=$items, unfittedItems=$unfittedItems, numberOfDecimals=$numberOfDecimals)"
    }

}