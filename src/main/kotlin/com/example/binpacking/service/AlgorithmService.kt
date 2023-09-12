package com.example.binpacking.service

import com.example.binpacking.entity.Item

enum class Algorithm {
    OLD, FFD, BFD, MFk
}

class AlgorithmService {
    fun packingWithFFD(packingTote: PackingService.PackingTote, packingItem: PackingService.PackingItem) {

        packingItem.items.map { item ->
            if (packingTote.totes.isEmpty()) {
                packingTote.addTote()
            }

            var packed = false

            for (tote in packingTote.totes) {
                if (tote.putItem(item)) {
                    tote.items.add(item)
                    packed = true
                    break
                } else tote.unfittedItems.add(item)
            }

            if (!packed) {
                with(packingTote) {
                    this.addTote()
                    this.totes.last().putItem(item)
                    this.totes.last().items.add(item)
                }
            }
        }
    }

    fun packingWithBFD(packingTote: PackingService.PackingTote, packingItem: PackingService.PackingItem) {

        packingItem.items.map { item ->
            if (packingTote.totes.isEmpty()) {
                packingTote.addTote()
            }

            var packed = false

            packingTote.totes.sortedBy { it.availSpace }

            for (tote in packingTote.totes) {
                if (tote.putItem(item)) {
                    tote.items.add(item)
                    packed = true
                    tote.availSpace -= item.getVolume()
                    break
                } else tote.unfittedItems.add(item)
            }

            if (!packed) {
                with(packingTote) {
                    this.addTote()
                    this.totes.last().putItem(item)
                    this.totes.last().items.add(item)
                    this.totes.last().availSpace -= item.getVolume()
                }
            }
        }
    }

    fun packingWithMFk(packingTote: PackingService.PackingTote, packingItem: PackingService.PackingItem, k: Int) {

        }
}