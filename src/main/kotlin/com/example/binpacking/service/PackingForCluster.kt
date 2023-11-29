package com.example.binpacking.service

import com.example.binpacking.entity.Item

class PackingForCluster(
    clusterSet: ClusterSet
) {
    private val algorithmType = Algorithm.FFD

    private val clusterList = clusterSet.clusters
    private val clusterPackingResult = groupPacking()

    fun getBinPackingResult(): BinpackingResult {
        return BinpackingResult(clusterPackingResult)
    }

    private fun groupPacking(): MutableList<PackingService> {
        val clusterPackingResult = mutableListOf<PackingService>()

        clusterList.forEach { cluster ->
            clusterPackingResult.add(Packing(cluster, algorithmType))
        }
        return clusterPackingResult
    }


    private fun Packing(cluster: ClusterInfo, algorithmType: Algorithm): PackingService {
        val packer = PackingService()

        cluster.skus.map { sku ->
            val name = sku.locationCode + "_" + sku.skuUid
            val item = Item(
                skuId = sku.skuUid,
                location = sku.locationCode,
                name = name,
                Item.Length(sku.cbmw.width, sku.cbmw.height, sku.cbmw.depth),
                weight = sku.cbmw.weight,
                quantity = sku.quantity,
                workId = ""
            )
            item.setDimension()
            packer.packingItem.addItem(item)

        }

        packer.packForTest(algorithm = algorithmType)
        return packer
    }
}
