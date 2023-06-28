package com.example.binpacking

class Item(
    var id: String,
    var location: String,
    var name: String,
    private var width: Double, private var height: Double, private var depth: Double, var weight: Double,
    private var quantity: Int
) {
    var rotationType: Int = 0
    var position: MutableList<Int> = START_POSITION
    private var numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS

    fun formatNumbers(numberOfDecimals: Int) {
        this.width = setToDecimal(this.width, numberOfDecimals)
        this.height = setToDecimal(this.height, numberOfDecimals)
        this.depth = setToDecimal(this.depth, numberOfDecimals)
        this.weight = setToDecimal(this.weight, numberOfDecimals)
        this.numberOfDecimals = numberOfDecimals
    }

    fun String(): String? {
        return java.lang.String.format(
            "%s(%sx%sx%s, weight: %s) pos(%s) rt(%s) vol(%s) count : %s",
            this.name, this.width, this.height, this.depth, this.weight, this.position,
            this.rotationType, this.getVolume(), this.quantity
        )
    }

    fun getVolume(): Double {
        val volume = this.width * this.height * this.depth
        return setToDecimal(volume, this.numberOfDecimals)
    }

    fun getDimension(): MutableList<Double> {
        val dimension = when (this.rotationType) {
            RotationType.WHD -> {
                mutableListOf(this.width, this.height, this.depth)
            }

            RotationType.WDH -> {
                mutableListOf(this.width, this.depth, this.height)
            }

            RotationType.HWD -> {
                mutableListOf(this.height, this.width, this.depth)
            }

            RotationType.HDW -> {
                mutableListOf(this.height, this.depth, this.width)
            }

            RotationType.DWH -> {
                mutableListOf(this.depth, this.width, this.height)
            }

            RotationType.DHW -> {
                mutableListOf(this.depth, this.height, this.width)
            }

            else -> mutableListOf()
        }

        return dimension
    }
}
