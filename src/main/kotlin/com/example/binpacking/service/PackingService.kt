package com.example.binpacking.service

import com.example.binpacking.entity.*

class PackingService {
    val packingTote = PackingTote()
    val packingItem = PackingItem()

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
        }
        }
    }
}
