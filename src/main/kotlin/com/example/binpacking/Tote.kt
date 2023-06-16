package com.example.binpacking

class Tote(name: String, width: Double, height: Double, depth: Double, maxWeight: Double) {
    var name: String = name
    var width: Double = width
    var height: Double = height
    var depth: Double = depth
    var maxWeight: Double = maxWeight
    var items: MutableList<Item> = mutableListOf()
    var unfittedItems: MutableList<Item> = mutableListOf()
    var numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS

    fun formatNumbers(numberOfDecimals: Int) {
        this.width = setToDecimal(this.width, numberOfDecimals)
        this.height = setToDecimal(this.height, numberOfDecimals)
        this.depth = setToDecimal(this.depth, numberOfDecimals)
        this.maxWeight = setToDecimal(this.maxWeight, numberOfDecimals)
        this.numberOfDecimals = numberOfDecimals
    }

    fun String(): String? {
        return java.lang.String.format("%s(%sx%sx%s, max_weight:%s) vol(%s)",
            this.name, this.width, this.height, this.depth, this.maxWeight, this.getVolume())
    }

    fun getVolume(): Double {
        val volume = this.width * this.height * this.depth
        return setToDecimal(volume, this.numberOfDecimals)
    }

    fun getTotalWeight(): Double {
        var totalWeight: Double = 0.0

        for (item in this.items) {
            totalWeight += item.weight
        }

        return totalWeight
    }

    fun putItem(item: Item, pivot: MutableList<Int>): Boolean {
        val rotationType = RotationType()
        var fit: Boolean = false
        val validItemPosition = item.position
        item.position = pivot

        for (i in 0 until rotationType.ALL.size) {
            item.rotationType = i
            val dimension = item.getDimension()

            if (
                this.width < pivot[0] + dimension[0] ||
                this.height < pivot[1] + dimension[1] ||
                this.depth < pivot[2] + dimension[2]
            )
                continue

            fit = true

            for (currentItemInTote in this.items) {
                if (intersect(currentItemInTote, item)) {
                    fit = false
                    break
                }
            }

            if (fit) {
                if (this.getTotalWeight() + item.weight > this.maxWeight) {
                    fit = false
                    return fit
                }

                this.items.add(item)
            }

            else {
                item.position = validItemPosition
            }

            return fit
        }

        if (!fit)
            item.position = validItemPosition

        return fit
    }
}