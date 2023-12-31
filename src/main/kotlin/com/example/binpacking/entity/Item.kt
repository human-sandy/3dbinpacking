package com.example.binpacking.entity

import com.example.binpacking.setToDecimal

data class Item(
    //아이템 정보 매개 변수
    val skuId: String,
    val location: String,
    val name: String,
    private val length: Length,
    var weight: Double,
    var quantity: Int,
    val workId: String,
) {

    private val numberOfDecimals = DEFAULT_NUMBER_OF_DECIMALS
    private var rotationType = RotationType.DEPTH_IS_LONGER_THAN_WIDTH // default: depth is longer than width

    lateinit var position: Point
    lateinit var cbm: Cbm

    data class Length(
        val length1: Double,
        val length2: Double,
        val length3: Double,
    )

    data class Cbm(
        val width: Double,
        val depth: Double,
        val height: Double,
    )

    data class Point(
        val x: Double,
        val y: Double,
        val z: Double,
    )

    fun getDimension(): Cbm {
        return this.cbm
    }

    fun getVolume(): Double {
        val volume = this.cbm.width * this.cbm.height * this.cbm.depth
        return setToDecimal(volume, this.numberOfDecimals)
    } // 부피 반환

    fun getArea(): Double {
        val area = this.cbm.width * this.cbm.depth
        return setToDecimal(area, this.numberOfDecimals)
    } //밑면 면적 반환

    fun setDimension() {
        var dimension = listOf(length.length1, length.length2, length.length3)
        dimension = dimension.sortedWith(Comparator<Double> { a, b ->
            when {
                a > b -> -1
                a < b -> 1
                else -> 0
            }
        })
        this.cbm = Cbm(dimension[1], dimension[0], dimension[2])
    }

    fun swapWidthDepth() {
        this.cbm = Cbm(this.cbm.depth, this.cbm.width, this.cbm.height)

        if (rotationType == RotationType.DEPTH_IS_LONGER_THAN_WIDTH) {
            rotationType = RotationType.WIDTH_IS_LONGER_THAN_DEPTH
        } else if (rotationType == RotationType.WIDTH_IS_LONGER_THAN_DEPTH) {
            rotationType = RotationType.DEPTH_IS_LONGER_THAN_WIDTH
        }
    }

    override fun toString(): String {
        return "Item(skuId='$skuId', location='$location', name='$name'," +
                " width=${this.cbm.width}, height=${this.cbm.height}, depth=${this.cbm.depth}, weight=${weight}," +
                " quantity=$quantity, rotationType=$rotationType, position=$position," +
                " numberOfDecimals=$numberOfDecimals)"
    }
}
