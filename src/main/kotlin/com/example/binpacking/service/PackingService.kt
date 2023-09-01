package com.example.binpacking.service

import com.example.binpacking.entity.*

class PackingService {
    val packingTote = PackingTote()
    val packingItem = PackingItem()
    val singleItemPacking = PackingTote()

    class PackingTote{
        var totes: MutableList<Tote> = mutableListOf()
        private var totalTotes: Int = 0

        private val toteSpec = ToteSpec(0.0)

        fun addTote() {
            val name = "tote_$totalTotes"
            val newTote = Tote(
                name,
                toteSpec.width,
                toteSpec.height,
                toteSpec.depth,
                toteSpec.weight
            )
            totalTotes += 1

            totes.add(newTote)
        }
    }

    class PackingItem {
        val items: MutableList<Item> = mutableListOf()
        val unfitItems: List<Item> = listOf()
        private var totalItems: Int = 0

        fun addItem(item: Item) {
            totalItems += 1
            items.add(item)
        }
    }


    private fun checkFit(tote: Tote, item: Item): Boolean {
        var fitted = false

        if (tote.items.isEmpty()) {
            val response = tote.putItem(item, START_POSITION)
            fitted = true

            if (!response) {
                tote.unfittedItems.add(item)
                fitted = false
            }
            return fitted
        } else {
            for (axis in 0 until 4) {
                fitted = pivotThreeDimensions(tote, item, axis)

                if (fitted)
                    break
            }

            if (!fitted)
                tote.unfittedItems.add(item)

            return fitted
        }
    }

    private fun pivotThreeDimensions(tote: Tote, item: Item, axis: Int): Boolean {
        val itemsInTote = tote.items
        var fitted: Boolean = false

        for (itemInTote in itemsInTote) {
            val dimension = itemInTote.getDimension()
            val width = dimension[0]
            val height = dimension[1]
            val depth = dimension[2]

            val pivot = when (axis) {
                Axis.WIDTH -> listOf(
                    itemInTote.position[0] + width.toInt(),
                    itemInTote.position[1],
                    itemInTote.position[2]
                )

                Axis.HEIGHT -> listOf(
                    itemInTote.position[0],
                    itemInTote.position[1] + height.toInt(),
                    itemInTote.position[2]
                )

                Axis.DEPTH -> listOf(
                    itemInTote.position[0],
                    itemInTote.position[1],
                    itemInTote.position[2] + depth.toInt()
                )

                else -> listOf(0, 0, 0)
            }

            if (tote.putItem(item, pivot)) {
                fitted = true
                break
            }
        }
        return fitted
    }

    private fun packToTote(item: Item, totes: List<Tote>): Boolean {
        val lastTote = totes[totes.size - 1]

        if (!checkFit(lastTote, item)) {
            singleItemPacking.addTote()
            return false
        }

        return true
    }

    private fun countDuplicates(targetList: MutableList<String>): Map<String, Int> {
        val occurrenceMap = mutableMapOf<String, Int>()

        targetList.forEach {name ->
            occurrenceMap[name] = occurrenceMap.getOrDefault(name, 0) + 1
        }

        return occurrenceMap
    }

    fun pack(
        biggerFirst: Boolean = true,
        numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS
    ){
        if (biggerFirst)
            packingItem.items.sortedBy{ it.getArea() }

        packingItem.items.map { item ->
            for (index in 0 until item.quantity) {
                var packed = false

                if (packingTote.totes.isEmpty()){
                    packingTote.addTote() }

                for (tote in packingTote.totes){
                    if(tote.putItem(item)) {
                        tote.items.add(item)
                        packed = true
                        break
                    } else
                        tote.unfittedItems.add(item)
                }

                if (!packed) {
                    with(packingTote){
                        this.addTote()
                        this.totes.last().putItem(item)
                        this.totes.last().items.add(item)}
    }

    private fun groupItemsInTote() {
        singleItemPacking.totes.map { tote ->
            val wholeItems = mutableListOf<Item>()
            val distinctItems = mutableListOf<Item>()
            val idList = mutableListOf<String>()
            val skuInfo = mutableMapOf<String, List<String>>()

            tote.items.map { item ->
                idList.add(item.id)

                if (!skuInfo.containsKey(item.id)) {
                    skuInfo[item.id] = arrayListOf(item.id, item.location)
                    wholeItems.add(item.copy())
                }
            }

            val duplicatesCount = countDuplicates(idList)
            packingTote.addTote()

            duplicatesCount.map { sku ->
                val quantity = sku.value
                val existingItem = wholeItems.find { item -> item.id == sku.key }

                if (existingItem != null) {
                    existingItem.quantity = quantity
                    distinctItems.add(existingItem)
                }
            }

            distinctItems.forEach { item ->
                packingTote.totes.last().items.add(item)

            }
        }
    }
}
