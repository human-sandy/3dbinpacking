package com.example.binpacking.entity

import com.example.binpacking.setToDecimal

data class Item(
    //아이템 정보 매개 변수
    val skuId: String,
    val location: String,
    val name: String,
    private val length: Length = Length(-1.0, -1.0, -1.0),
    var weight: Double,
    var quantity: Int,
    val workId: String
) {
    //아이템 프로퍼티
    private var rotationType: RotationType = RotationType.LONGER_DEPTH // default: depth is longer than width
    var position: Pivot = Pivot(-1.0, -1.0, -1.0)
    var cbm: Cbm = Cbm(-1.0, -1.0, -1.0)
    private val numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS

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

    data class Pivot(
        val x: Double,
        val y: Double,
        val z: Double
    )

    fun setDimension() {
        var dimension = listOf(length.length1, length.length2, length.length3)
        dimension = dimension.sortedWith(Comparator<Double>{ a, b ->
            when {
                a > b -> -1
                a < b -> 1
                else -> 0
            }
        })
        this.cbm = Cbm(dimension[1], dimension[0], dimension[2])
    } // width, depth, height 확정

    fun getDimension(): Cbm {
        return Cbm(this.cbm.width, this.cbm.depth, this.cbm.height)
    }

    fun formatNumbers(numberOfDecimals: Int) {
        val width = setToDecimal(this.cbm.width, numberOfDecimals)
        val height = setToDecimal(this.cbm.height, numberOfDecimals)
        val depth = setToDecimal(this.cbm.depth, numberOfDecimals)
        this.weight = setToDecimal(this.weight, numberOfDecimals)

        this.cbm = Cbm(width, depth, height)
    } // 아이템 정보 가공

    fun widthDepthSwitch() {
        this.cbm = Cbm(this.cbm.depth, this.cbm.width, this.cbm.height)

        if (rotationType == RotationType.LONGER_DEPTH){
            rotationType = RotationType.LONGER_WIDTH
        }
        else if(rotationType == RotationType.LONGER_WIDTH){
            rotationType = RotationType.LONGER_DEPTH
        }
    }

    fun getVolume(): Double {
        val volume = this.cbm.width * this.cbm.height * this.cbm.depth
        return setToDecimal(volume, this.numberOfDecimals)
    } // 부피 반환

    fun getArea(): Double{
        val area = this.cbm.width * this.cbm.depth
        return setToDecimal(area, this.numberOfDecimals)
    } //밑면 면적 반환

    /*
    fun copy(): Item {
        return Item(skuId, location, name, Length(this.cbm.width, this.cbm.height, this.cbm.depth), weight, quantity, workId)
    }
    */

    override fun toString(): String {
        return "Item(skuId='$skuId', location='$location', name='$name'," +
                " width=${this.cbm.width}, height=${this.cbm.height}, depth=${this.cbm.depth}, weight=${weight}," +
                " quantity=$quantity, rotationType=$rotationType, position=$position," +
                " numberOfDecimals=$numberOfDecimals)"
    }
}
