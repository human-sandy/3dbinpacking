package com.example.binpacking

class Tote(name: String, width: Double, height: Double, depth: Double, maxWeight: Double) {
    private var name: String
    private var width: Double
    private var height: Double
    private var depth: Double
    private var maxWeight: Double
    var items: MutableList<Item>
    var unfittedItems: MutableList<Item>
    private var numberOfDecimals: Int

    init {
        this.name = name
        this.width = width
        this.height = height
        this.depth = depth
        this.maxWeight = maxWeight
        items = mutableListOf()
        unfittedItems = mutableListOf()
        numberOfDecimals = DEFAULT_NUMBER_OF_DECIMALS
    }

    fun formatNumbers(numberOfDecimals: Int) {
        this.width = setToDecimal(this.width, numberOfDecimals)
        this.height = setToDecimal(this.height, numberOfDecimals)
        this.depth = setToDecimal(this.depth, numberOfDecimals)
        this.maxWeight = setToDecimal(this.maxWeight, numberOfDecimals)
        this.numberOfDecimals = numberOfDecimals
    }

    fun String(): String? {
        return java.lang.String.format(
            "%s(%sx%sx%s, max_weight:%s) vol(%s)",
            this.name, this.width, this.height, this.depth, this.maxWeight, this.getVolume()
        )
    }

    private fun getVolume(): Double {
        return setToDecimal(this.width * this.height * this.depth, this.numberOfDecimals)
    }

    private fun getTotalWeight(): Double {
        return this.items.sumOf { item -> item.weight }
    }

    fun putItem(item: Item, pivot: MutableList<Int>): Boolean {
        var fit = false
        val validItemPosition = item.position
        item.position = pivot

        for (i in 0 until RotationType.ALL.size) {
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
                    return false
                }

                this.items.add(item)
            } else {
                item.position = validItemPosition
            }

            return fit
        }

        item.position = validItemPosition

        return false
    }
}