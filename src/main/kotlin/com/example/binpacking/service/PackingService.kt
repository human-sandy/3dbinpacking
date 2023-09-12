package com.example.binpacking.service

import com.example.binpacking.entity.DEFAULT_NUMBER_OF_DECIMALS
import com.example.binpacking.entity.Item
import com.example.binpacking.entity.Tote
import com.example.binpacking.entity.ToteSpec

class PackingService {
    val packingTote = PackingTote()
    val packingItem = PackingItem()
    val singleItemPacking = PackingTote()

    class PackingTote {
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
            for (index in 0 until item.quantity) {
                // item 개별 확인용
                totalItems += 1
                val individualItem = item.copy()
                individualItem.getDimension()
                items.add(individualItem)
            }
        }
    }


    private fun countDuplicates(targetList: MutableList<String>): Map<String, Int> {
        val occurrenceMap = mutableMapOf<String, Int>()

        targetList.forEach { name ->
            occurrenceMap[name] = occurrenceMap.getOrDefault(name, 0) + 1
        }

        return occurrenceMap
    }

    fun pack(
        biggerFirst: Boolean = true,
        numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS,
        algorithm: Algorithm
    ) {
        val algorithmService = AlgorithmService()
        if (biggerFirst)
            packingItem.items.sortedBy { it.getArea() }

        packingItem.items.map { item ->
            if (packingTote.totes.isEmpty()) {
                packingTote.addTote()
            }

            when (algorithm) {
                Algorithm.OLD -> println("OLD")
                Algorithm.FFD -> {
                    algorithmService.packingWithFFD(packingTote, item)
                }
                Algorithm.BFD -> {
                    algorithmService.packingWithBFD(packingTote, item)
                }
            }
        }

        groupItemsInTote()
    }

    private fun groupItemsInTote() {
        singleItemPacking.totes.map { tote ->
            val wholeItems = mutableListOf<Item>()
            val distinctItems = mutableListOf<Item>()
            val idList = mutableListOf<String>()
            val skuInfo = mutableMapOf<String, List<String>>()

            tote.items.map { item ->
                idList.add(item.skuId)

                if (!skuInfo.containsKey(item.skuId)) {
                    skuInfo[item.skuId] = arrayListOf(item.skuId, item.location)
                    wholeItems.add(item.copy())
                }
            }

            val duplicatesCount = countDuplicates(idList)
            packingTote.addTote()

            duplicatesCount.map { sku ->
                val quantity = sku.value
                val existingItem = wholeItems.find { item -> item.skuId == sku.key }

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
