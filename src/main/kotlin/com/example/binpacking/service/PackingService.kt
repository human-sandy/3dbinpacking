package com.example.binpacking.service

import com.example.binpacking.entity.Item
import com.example.binpacking.entity.Tote
import com.example.binpacking.entity.ToteSpec

class PackingService {
    val packingTote = PackingTote()
    val packingItem = PackingItem()
    val singleItemPackingTote = PackingTote()

    class PackingTote {
        val totes: MutableList<Tote> = mutableListOf()
        private val toteSpec = ToteSpec(0.0)

        fun addTote() {
            val name = "tote_${totes.size}"
            val newTote = Tote(
                name,
                toteSpec.width,
                toteSpec.height,
                toteSpec.depth,
                toteSpec.weight
            )
            totes.add(newTote)
        }
    }

    class PackingItem {
        val items: MutableList<Item> = mutableListOf()

        fun addItem(item: Item) {
            for (index in 0 until item.quantity) {
                // item 개별 확인용
                val individualItem = item.copy()
                individualItem.setDimension()
                items.add(individualItem)
            }
        }
    }

    private fun countDuplicates(singleItemSkuIdlist: MutableList<String>): Map<String, Int> {
        val countMap = mutableMapOf<String, Int>()

        singleItemSkuIdlist.forEach { skuId ->
            countMap[skuId] = countMap.getOrDefault(skuId, 0) + 1
        }

        return countMap
    }

    fun pack(
        biggerFirst: Boolean = true,
        algorithm: Algorithm
    ) {
        val algorithmService = AlgorithmService()
        if (biggerFirst) {
            packingItem.items.sortByDescending { it.getArea() }
        }

        when (algorithm) {
            Algorithm.FFD -> {
                algorithmService.packingWithFFD(singleItemPackingTote, packingItem)
            }

            Algorithm.BFD -> {
                algorithmService.packingWithBFD(singleItemPackingTote, packingItem)
            }

            Algorithm.MFK -> {
                algorithmService.packingWithMFK(singleItemPackingTote, packingItem, 1)
            }
        }
        groupItemsInTote()
    }

    fun packForTest(
        biggerFirst: Boolean = true,
        algorithm: Algorithm
    ) {
        val algorithmService = AlgorithmService()
        if (biggerFirst)
            packingItem.items.sortByDescending { it.getArea() }

        when (algorithm) {
            Algorithm.FFD -> {
                algorithmService.packingWithFFD(singleItemPackingTote, packingItem)
            }

            Algorithm.BFD -> {
                algorithmService.packingWithBFD(singleItemPackingTote, packingItem)
            }

            Algorithm.MFK -> {
                algorithmService.packingWithMFK(singleItemPackingTote, packingItem, 1)
            }
        }
    }

    private fun groupItemsInTote() {
        singleItemPackingTote.totes.map { tote ->
            packingTote.addTote()
            val singleSkuIdList = mutableListOf<String>()
            val groupedSkuIdList = mutableListOf<String>()
            val groupedItems = mutableListOf<Item>()

            tote.items.map { item ->
                singleSkuIdList.add(item.skuId)

                if (!groupedSkuIdList.contains(item.skuId)) {
                    groupedSkuIdList.add(item.skuId)
                    groupedItems.add(item.copy())
                }
            }

            val duplicatesCount = countDuplicates(singleSkuIdList)

            duplicatesCount.map { sku ->
                val quantity = sku.value
                val updatedItem: Item? = groupedItems.find { item -> item.skuId == sku.key }
                updatedItem?.let {
                    it.quantity = quantity
                }
            }

            groupedItems.forEach { item ->
                packingTote.totes.last().items.add(item)

            }
        }
    }
}