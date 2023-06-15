package com.example.binpacking

class Item(id: String, location: String, name: String, width: Double, height: Double, depth: Double, weight: Double, quantity: Int) {
    var itemId: String = id
    var location: String = location
    var name: String = name
    var width: Double = width
    var height: Double = height
    var depth: Double = depth
    var weight: Double = weight
    var quantity: Int = quantity
    var rotationType: Int = 0
    var position: MutableList<Int> = START_POSITION
    var numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS

    fun formatNumbers(numberOfDecimals: Int) {
        this.width = setToDecimal(this.width, numberOfDecimals)
        this.height = setToDecimal(this.height, numberOfDecimals)
        this.depth = setToDecimal(this.depth, numberOfDecimals)
        this.weight = setToDecimal(this.weight, numberOfDecimals)
        this.numberOfDecimals = numberOfDecimals
    }

    fun String(): String? {
        return java.lang.String.format("%s(%sx%sx%s, weight: %s) pos(%s) rt(%s) vol(%s) count : %s",
            this.name, this.width, this.height, this.depth, this.weight, this.position,
            this.rotationType, this.getVolume(), this.quantity)
    }

    fun getVolume(): Double {
        val volume = this.width * this.height * this.depth
        return setToDecimal(volume, this.numberOfDecimals)
    }

    fun getDimension() : MutableList<Double> {
        val rotationType = RotationType()

        val dimension = when (this.rotationType) {
            rotationType.RT_WHD -> {
                mutableListOf(this.width, this.height, this.depth)
            }

            rotationType.RT_WDH -> {
                mutableListOf(this.width, this.depth, this.height)
            }

            rotationType.RT_HWD -> {
                mutableListOf(this.height, this.width, this.depth)
            }

            rotationType.RT_HDW -> {
                mutableListOf(this.height, this.depth, this.width)
            }

            rotationType.RT_DWH -> {
                mutableListOf(this.depth, this.width, this.height)
            }

            rotationType.RT_DHW -> {
                mutableListOf(this.depth, this.height, this.width)
            }

            else -> mutableListOf()
        }

        return dimension
    }
}
