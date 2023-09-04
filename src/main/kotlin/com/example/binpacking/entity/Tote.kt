package com.example.binpacking.entity

import java.util.LinkedList

class Tote(
    val name: String,
    var width: Double,
    var height: Double,
    var depth: Double,
    var maxWeight: Double)
{
    val items: MutableList<Item> = mutableListOf()
    val unfittedItems: MutableList<Item> = mutableListOf()
    private var numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS
    private val priorPivot: List<Double> = listOf(0.0, 0.0, 0.0)
    private var pivots: LinkedList<List<Double>> = LinkedList(listOf(priorPivot))
    private var vertexs = LinkedList<List<Double>>()


    private fun getTotalWeight(): Double {
        return this.items.sumOf { item -> item.weight }
    }

    private fun checkSize(item:Item, pivot:List<Double>): Boolean {
        var fit = false
        if (
            width >= pivot[0] + item.width &&
            depth >= pivot[1] + item.depth &&
            height >= pivot[2] + item.height
        ) { fit = true
            return fit}
        else{ return fit }
    }

    private fun sumPoints(pivot: List<Double>, gap:List<Double>): List<Double> {
        return listOf(pivot[0]+gap[0], pivot[1]+gap[1], pivot[2]+gap[2])
    }

    private fun addPivots(item:Item, pivot:List<Double>) {
        pivots.remove(pivot)
        var newPivot = sumPoints(pivot, listOf(item.width, 0.0, 0.0))
        if (!pivots.contains(newPivot)){
            pivots.add(newPivot)
        }
        newPivot = sumPoints(pivot, listOf(0.0, item.depth, 0.0))
        if (!pivots.contains(newPivot)){
            pivots.add(newPivot)
        }
        newPivot = sumPoints(pivot, listOf(0.0, 0.0, item.height))
        if (!pivots.contains(newPivot)){
            pivots.add(newPivot)
        }
                // item.position == pivot but 가독성을 위해

        vertexs.add(sumPoints(pivot, listOf(item.width, item.depth, 0.0)))
        vertexs.add(sumPoints(pivot, listOf(item.width, item.depth, item.height)))
    }

    fun putItem(item: Item): Boolean {
        var fit: Boolean = false
        if (getTotalWeight() + item.weight < maxWeight){

            for (pivot in pivots){
                if (checkSize(item, pivot)){
                    fit = true
                    item.position = pivot.toMutableList()
                    addPivots(item, pivot)
                    break
                }
                else{ item.widthDepthSwitch()
                    if (checkSize(item, pivot)){
                        fit = true
                        item.position = pivot.toMutableList()
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