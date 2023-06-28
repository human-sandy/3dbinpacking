package com.example.binpacking

class Packing {
    var totes: MutableList<Tote> = mutableListOf()
    var totalTotes: Int = 0
    var items: MutableList<Item> = mutableListOf()
    var totalItems: Int = 0
    var unfitItems: MutableList<Item> = mutableListOf()

    private fun addTote(): Boolean {
        val name = "tote_" + this.totalTotes.toString()
        val newTote = Tote(
            name,
            ToteSpec.WIDTH.toDouble(),
            ToteSpec.HEIGHT.toDouble(),
            ToteSpec.DEPTH.toDouble(),
            ToteSpec.WEIGHT.toDouble()
        )
        this.totalTotes += 1

        return this.totes.add(newTote)
    }

    fun addItem(item: Item): Boolean {
        this.totalItems = this.items.size + 1

        return this.items.add(item)
    }

    private fun checkFit(tote: Tote, item: Item): Boolean {
        var fitted = false

        if (tote.items.size == 0) {
            val response = tote.putItem(item, START_POSITION)
            fitted = true

            if (!response) {
                tote.unfittedItems.add(item)
                fitted = false
            }

            return fitted
        } else {
            for (axis in 0..3) {
                val itemsInTote = tote.items

                for (itemInTote in itemsInTote) {
                    var pivot = mutableListOf(0, 0, 0)
                    val dimension = itemInTote.getDimension()
                    val w = dimension[0]
                    val h = dimension[1]
                    val d = dimension[2]

                    when (axis) {
                        Axis.WIDTH -> pivot = mutableListOf(
                            itemInTote.position[0] + w.toInt(),
                            itemInTote.position[1],
                            itemInTote.position[2]
                        )

                        Axis.HEIGHT -> pivot = mutableListOf(
                            itemInTote.position[0],
                            itemInTote.position[1] + h.toInt(),
                            itemInTote.position[2]
                        )

                        Axis.DEPTH -> pivot = mutableListOf(
                            itemInTote.position[0],
                            itemInTote.position[1],
                            itemInTote.position[2] + d.toInt()
                        )
                    }

                    if (tote.putItem(item, pivot)) {
                        fitted = true
                        break
                    }
                }

                if (fitted)
                    break
            }

            if (!fitted)
                tote.unfittedItems.add(item)

            return fitted
        }
    }

    private fun packToTote(item: Item): Int {
        var response = 0

        val lastTote = this.totes[this.totes.size - 1]
        val fitted = this.checkFit(lastTote, item)

        if (!fitted) {
            this.addTote()
            response = -1
        }

        return response
    }

    fun pack(
        biggerFirst: Boolean = false,
        numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS
    ) {

        for (item in this.items)
            item.formatNumbers(numberOfDecimals)

        if (biggerFirst)
            this.items = this.items.reversed().toMutableList()
        this.items = this.items.sortedWith(compareBy({ it.id }, { it.getVolume() })).toMutableList()

        this.addTote()

        this.items.forEach { item ->
            val response = this.packToTote(item)

            if (response < 0)
                this.packToTote(item)
        }
    }
}