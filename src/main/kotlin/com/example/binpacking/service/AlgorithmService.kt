package com.example.binpacking.service

import com.example.binpacking.entity.Item

enum class Algorithm {
    OLD, FFD, BFD, MFK
}

class AlgorithmService {
    fun packingWithFFD(singleItemPackingTote: PackingService.PackingTote, packingItem: PackingService.PackingItem) {

        packingItem.items.map { item ->
            if (singleItemPackingTote.totes.isEmpty()) {
                singleItemPackingTote.addTote()
            }
            var packed = false

            for (tote in singleItemPackingTote.totes) {
                if (tote.putItem(item)) {
                    tote.items.add(item)
                    packed = true
                    break
                } else tote.unfittedItems.add(item)
            }

            if (!packed) {
                with(singleItemPackingTote) {
                    this.addTote()
                    this.totes.last().putItem(item)
                    this.totes.last().items.add(item)
                }
            }
        }
    }


    fun packingWithBFD(singleItemPackingTote: PackingService.PackingTote, packingItem: PackingService.PackingItem) {

        packingItem.items.map { item ->
            if (singleItemPackingTote.totes.isEmpty()) {
                singleItemPackingTote.addTote()
            }
            var packed = false

            singleItemPackingTote.totes.sortedBy { it.availSpace }

            for (tote in singleItemPackingTote.totes) {
                if (tote.putItem(item)) {
                    tote.items.add(item)
                    packed = true
                    tote.availSpace -= item.getVolume()
                    break
                } else tote.unfittedItems.add(item)
            }

            if (!packed) {
                with(singleItemPackingTote) {
                    this.addTote()
                    this.totes.last().putItem(item)
                    this.totes.last().items.add(item)
                }
            }
        }


    }

    fun packingWithMFK(singleItemPackingTote: PackingService.PackingTote, packingItem: PackingService.PackingItem, k: Int) {

        while (packingItem.items.size != 0) {
            singleItemPackingTote.addTote()
            val tote = singleItemPackingTote.totes.last()

            with(tote) {
                for (i in 0 until k) {
                    if (this.putItem(packingItem.items.first())) {
                        val item = packingItem.items.removeFirst()
                        tote.items.add(item)
                    } else break
                }
                if(packingItem.items.isEmpty())
                    return

                while (this.putItem(packingItem.items.last())) {
                    val item = packingItem.items.removeLast()
                    tote.items.add(item)

                    if(packingItem.items.isNotEmpty())
                        continue
                    else break
                }
            }
        }
    }
}


