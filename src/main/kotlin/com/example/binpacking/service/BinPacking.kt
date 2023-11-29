package com.example.binpacking.service

class CbmwInfo(var width: Double, var height: Double, var depth: Double, var weight: Double)

class SkuInfo(var skuUid: String, var quantity: Int, var locationCode: String, var cbmw: CbmwInfo)

class WorkGroupInfo(var workGroupUid: String, var skus: MutableList<SkuInfo>)

class PickingSku(var skuUid: String, var quantity: Int)

class Picking(var workGroupUid: String, var pickingSku: PickingSku)


class BinpackingResult(val groupPicking: MutableList<PackingService>)

class ClusterSet(val clusters: List<ClusterInfo>, val fileName: String)

class ClusterInfo(val clusterUid: String, var skus: MutableList<SkuInfo>)
