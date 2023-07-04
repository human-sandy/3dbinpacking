package com.example.binpacking.entity

import com.example.binpacking.setToDecimal

class Item(
    val id: String,
    val location: String,
    val name: String,
    var width: Double, var height: Double, var depth: Double, var weight: Double,
    var quantity: Int
) {
    var rotationType: Int = 0
    var position: MutableList<Int> = START_POSITION.toMutableList()
    private var numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS

    fun formatNumbers(numberOfDecimals: Int) {
        width = setToDecimal(this.width, numberOfDecimals)
        height = setToDecimal(this.height, numberOfDecimals)
        depth = setToDecimal(this.depth, numberOfDecimals)
        weight = setToDecimal(this.weight, numberOfDecimals)
        this.numberOfDecimals = numberOfDecimals
    }

    fun getVolume(): Double {
        val volume = width * height * depth
        return setToDecimal(volume, this.numberOfDecimals)
    }

    fun getDimension(): List<Double> {
        val dimension = when (this.rotationType) {
            RotationType.WHD -> listOf(this.width, this.height, this.depth)

            RotationType.WDH -> listOf(this.width, this.depth, this.height)

            RotationType.HWD -> listOf(this.height, this.width, this.depth)

            RotationType.HDW -> listOf(this.height, this.depth, this.width)

            RotationType.DWH -> listOf(this.depth, this.width, this.height)

            RotationType.DHW -> listOf(this.depth, this.height, this.width)

            else -> listOf()
        }

        return dimension
    }

    override fun toString(): String {
        return "Item(id='$id', location='$location', name='$name', width=$width, height=$height, depth=$depth, weight=$weight, quantity=$quantity, rotationType=$rotationType, position=$position, numberOfDecimals=$numberOfDecimals)"
    }
}
