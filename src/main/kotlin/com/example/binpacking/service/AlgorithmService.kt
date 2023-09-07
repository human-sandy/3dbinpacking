package com.example.binpacking.service

import com.example.binpacking.entity.Item

enum class Algorithm {
    OLD, FFD, BFD
}

class AlgorithmService {
    fun packingWithFFD(packingTote: PackingService.PackingTote, packingItem: Item) {
        var packed = false

        for (tote in packingTote.totes) {
            if (tote.putItem(packingItem)) {
                tote.items.add(packingItem)
                packed = true
                break
            }
            else tote.unfittedItems.add(packingItem)
        }

        if (!packed) {
            with(packingTote) {
                this.addTote()
                this.totes.last().putItem(packingItem)
                this.totes.last().items.add(packingItem)
            }
        }
    }
}