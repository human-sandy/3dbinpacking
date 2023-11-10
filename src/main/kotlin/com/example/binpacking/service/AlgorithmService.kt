package com.example.binpacking.service

enum class Algorithm {
    FFD, BFD, MFK
}

class AlgorithmService {
    fun packingWithFFD(singleItemPackingTote: PackingService.PackingTote, packingItem: PackingService.PackingItem) {

        packingItem.items.map { item ->
            var packed = false

            if (singleItemPackingTote.totes.isEmpty()) {
                singleItemPackingTote.addTote()
            }

            for (tote in singleItemPackingTote.totes) {
                if (tote.putItem(item)) {
                    packed = true
                    tote.items.add(item)
                    tote.remainedVolume -= item.getVolume()
                    break
                } else tote.unfittedItems.add(item)
            }

            if (!packed) {
                with(singleItemPackingTote) {
                    this.addTote()
                    this.totes.last().putItem(item)
                    this.totes.last().items.add(item)
                    this.totes.last().remainedVolume -= item.getVolume()
                }
            }
        }
    }

    fun packingWithBFD(singleItemPackingTote: PackingService.PackingTote, packingItem: PackingService.PackingItem) {

        packingItem.items.map { item ->
            var packed = false

            if (singleItemPackingTote.totes.isEmpty()) {
                singleItemPackingTote.addTote()
            }

            singleItemPackingTote.totes.sortBy { it.remainedVolume }

            for (tote in singleItemPackingTote.totes) {
                if (tote.putItem(item)) {
                    packed = true
                    tote.items.add(item)
                    tote.remainedVolume -= item.getVolume()
                    break
                } else tote.unfittedItems.add(item)
            }

            if (!packed) {
                with(singleItemPackingTote) {
                    this.addTote()
                    this.totes.last().putItem(item)
                    this.totes.last().items.add(item)
                    this.totes.last().remainedVolume -= item.getVolume()
                }
            }
        }
    }

    fun packingWithMFK(
        singleItemPackingTote: PackingService.PackingTote,
        packingItem: PackingService.PackingItem,
        k: Int
    ) {
        while (packingItem.items.isNotEmpty()) {
            singleItemPackingTote.addTote()
            val tote = singleItemPackingTote.totes.last()

            // k개의 큰 아이템 선적재
            for (i in 0 until k) {
                if (packingItem.items.isEmpty()) break

                val item = packingItem.items.first()
                if (tote.putItem(item)) {
                    packingItem.items.removeFirst()
                    tote.items.add(item)
                    tote.remainedVolume -= item.getVolume()
                } else break
            }

            // 남은 공간에 작은 아이템 가능한 만큼 적재
            while (packingItem.items.isNotEmpty()) {
                val item = packingItem.items.last()
                if (tote.putItem(item)) {
                    packingItem.items.removeLast()
                    tote.items.add(item)
                    tote.remainedVolume -= item.getVolume()
                } else break
            }
        }
    }
}


