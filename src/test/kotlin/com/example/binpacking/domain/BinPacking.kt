package com.example.binpacking.domain

class CbmwInfo(var width: Double, var height: Double, var depth: Double, var weight: Double)

class SkuInfo(var skuUid: String, var quantity: Int, var locationCode: String, var cbmw: CbmwInfo)

class WorkGroupInfo(var workGroupUid: String, var skus: MutableList<SkuInfo>)

class PickingSku(var skuUid: String, var quantity: Int)

class Picking(var workGroupUid: String, var pickingSku: PickingSku)