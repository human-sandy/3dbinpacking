package com.example.binpacking.entity

import com.example.binpacking.setToDecimal
class Item(
    //아이템 정보 매개 변수
    val id: String,
    val location: String,
    val name: String,
    var length1: Double, var length2: Double, var length3: Double, var weight: Double,
    var quantity: Int
) {
    //아이템 프로퍼티
    var rotationType: Int = 0 // default: width is longer than depth
    var position: MutableList<Double> = mutableListOf(-1.0, -1.0, -1.0)
    private var numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS
    var width: Double = 0.0
    var depth: Double = 0.0
    var height: Double = 0.0

    fun getDimension(): List<Double> {
        var dim = listOf(length1, length2, length3)
        dim = dim.sortedWith(Comparator<Double>{ a, b ->
            when {
                a > b -> -1
                a < b -> 1
                else -> 0
            }
        })
        this.depth = dim[0]
        this.width = dim[1]
        this.height = dim[2]
        return dim
    } // width, depth, height 확정

    fun formatNumbers(numberOfDecimals: Int) {
        width = setToDecimal(width, numberOfDecimals)
        height = setToDecimal(height, numberOfDecimals)
        depth = setToDecimal(depth, numberOfDecimals)
        weight = setToDecimal(weight, numberOfDecimals)
        this.numberOfDecimals = numberOfDecimals
    } // 아이템 정보 가공

    fun widthDepthSwitch(): List<Double> {
        var temp = this.width
        this.width = this.depth
        this.depth = temp

        if (rotationType == 0){
            rotationType = 1
        }
        else if(rotationType == 1){
            rotationType = 0
        }
        return listOf(width, depth, height)
    }

    fun getVolume(): Double {
        val volume = width * height * depth
        return setToDecimal(volume, this.numberOfDecimals)
    } // 부피 반환

    fun getArea(): Double{
        val area = width * depth
        return setToDecimal(area, this.numberOfDecimals)
    } //밑면 면적 반환

    fun copy(): Item {
        return Item(id, location, name, width, height, depth, weight, quantity)
    }

    override fun toString(): String {
        return "Item(id='$id', location='$location', name='$name', width=$width, height=$height, depth=$depth, weight=$weight, quantity=$quantity, rotationType=$rotationType, position=$position, numberOfDecimals=$numberOfDecimals)"
    }
}
