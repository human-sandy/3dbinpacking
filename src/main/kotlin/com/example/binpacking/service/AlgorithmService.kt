package com.example.binpacking.service

enum class Algorithm {
    FFD, BFD, MFK
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
                    tote.remainVolume -= item.getVolume()
                    break
                } else tote.unfittedItems.add(item)
            }

            if (!packed) {
                with(singleItemPackingTote) {
                    this.addTote()
                    this.totes.last().putItem(item)
                    this.totes.last().items.add(item)
                    this.totes.last().remainVolume -= item.getVolume()
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

            singleItemPackingTote.totes.sortBy { it.remainVolume }

            for (tote in singleItemPackingTote.totes) {
                if (tote.putItem(item)) {
                    tote.items.add(item)
                    packed = true
                    tote.remainVolume -= item.getVolume()
                    break
                } else tote.unfittedItems.add(item)
            }

            if (!packed) {
                with(singleItemPackingTote) {
                    this.addTote()
                    this.totes.last().putItem(item)
                    this.totes.last().items.add(item)
                    this.totes.last().remainVolume -= item.getVolume()
                }
            }
        }
    }

    fun packingWithMFK(
        singleItemPackingTote: PackingService.PackingTote,
        packingItem: PackingService.PackingItem,
        k: Int
    ) {

        while (packingItem.items.size != 0) {
            singleItemPackingTote.addTote()
            val tote = singleItemPackingTote.totes.last()

            with(tote) {
                for (i in 0 until k) {
                    if (packingItem.items.isNotEmpty()) {
                        if (this.putItem(packingItem.items.first())) {
                            val item = packingItem.items.removeFirst()
                            this.remainVolume -= item.getVolume()
                            tote.items.add(item)
                        } else break
                    } else break }

                while (packingItem.items.isNotEmpty()) {
                    if(this.putItem(packingItem.items.last())) {
                        val item = packingItem.items.removeLast()
                        this.remainVolume -= item.getVolume()
                        tote.items.add(item)
                    } else break
                }
            }
        }
    }
}


