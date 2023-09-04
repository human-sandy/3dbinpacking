package com.example.binpacking.service

import com.example.binpacking.entity.*

class PackingService {
    val packingTote = PackingTote()
    val packingItem = PackingItem()

    class PackingTote {
        val totes: MutableList<Tote> = mutableListOf()
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
                items.add(individualItem)}
        }
    }

    private fun checkFit(tote: Tote, item: Item): Boolean {
        var fitted = false

        // 한 토트 안에서의 경우
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

                if (fitted) // 하나만 fit 해도 ok
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
            } // 아이템이 적재될 수 있는지 확인할 때 아이템의 길이가 2번 더해짐
        }
        return fitted
    }

    private fun packToTote(item: Item, totes: List<Tote>): Boolean {
        val lastTote = totes[totes.size - 1]

        if (!checkFit(lastTote, item)) {
            packingTote.addTote()
            return false
        }

        return true
    } // tote를 더해주는 역할만 함

    fun pack(
        biggerFirst: Boolean = false,
        numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS
    ) {
        packingItem.items.map { item -> item.formatNumbers(numberOfDecimals) }

        if (biggerFirst)
            packingItem.items.reversed()
        packingItem.items.sortedWith(compareBy({ it.id }, { it.getVolume() }))

        packingTote.addTote()

        packingItem.items.map { item ->
                val sku = item.copy()
                sku.quantity = 1
                val response = packToTote(sku, packingTote.totes)

                if (!response)
                    packToTote(sku, packingTote.totes)
            }
        }
    }

