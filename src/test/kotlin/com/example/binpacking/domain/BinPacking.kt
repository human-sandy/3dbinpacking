package com.example.binpacking.domain

class CbmwInfo(width: Double, height: Double, depth: Double, weight: Double) {
    var width: Double = width
    var height: Double = height
    var depth: Double = depth
    var weight: Double = weight
}

class SkuInfo(skuUid: String, quantity: Int, locationCode: String, cbmw: CbmwInfo) {
    var skuUid: String = skuUid
    var quantity: Int = quantity
    var locationCode: String = locationCode
    var cbmw: CbmwInfo = cbmw
}

class WorkGroupInfo(workGroupUid: String, skus: MutableList<SkuInfo>) {
    var workGroupUid: String = workGroupUid
    var skus: MutableList<SkuInfo> = skus
}

class PickingSku(skuUid: String, locationCode: String, quantity: Int) {
    var skuUid: String = skuUid
    var locationCode: String = locationCode
    var quantity: Int = quantity
}

class Picking(workGroupUid: String, pickingSku: PickingSku) {
    var workGroupUid: String = workGroupUid
    var pickingSku: PickingSku = pickingSku
}