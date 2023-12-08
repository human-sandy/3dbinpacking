package com.example.binpacking.service

class CbmwInfo(var width: Double, var height: Double, var depth: Double, var weight: Double)

class SkuInfo(
    val skuUid: String,
    val quantity: Int,
    val locationCode: String,
    var cbmw: CbmwInfo,
    var x: Double,
    var y: Double,

    var position: Position<Double>
)

data class Position<T : Number>(val x: T, val y: T)

class WorkGroupInfo(var workGroupUid: String, var skus: MutableList<SkuInfo>)

class PickingSku(var skuUid: String, var quantity: Int)

class Picking(var workGroupUid: String, var pickingSku: PickingSku)


class BinpackingResult(val groupPicking: MutableList<PackingService>)

class ClusterSet(val clusters: List<ClusterInfo>, val fileName: String)

class ClusterInfo(val clusterUid: String, var skus: MutableList<SkuInfo>)
