package com.example.binpacking

import com.example.binpacking.domain.*
import kotlin.random.Random

fun main(args: Array<String>) {
    val workGroupList: MutableList<WorkGroupInfo> = createWorkGroup()

    for (workGroup in workGroupList) {
        val packingResult = createPicking(workGroup)
        val pickingGroup = createPickingGroups(packingResult, workGroup.workGroupUid)
    }
}

fun createWorkGroup(): MutableList<WorkGroupInfo> {
    var workGroupList : MutableList<WorkGroupInfo> = mutableListOf()
    var skuList : MutableList<SkuInfo> = mutableListOf()

    val apple = SkuInfo(skuUid = "apple", quantity = 0, locationCode = "a-01-01",
        cbmw = CbmwInfo(width=10.0, height=50.0, depth=80.0, weight=30.0)
    )
    val banana = SkuInfo(skuUid = "banana", quantity = 0, locationCode = "a-03-01",
        cbmw = CbmwInfo(width=50.0, height=80.0, depth=80.0, weight=50.0)
    )
    val mango = SkuInfo(skuUid = "mango", quantity = 0, locationCode = "a-02-02",
        cbmw = CbmwInfo(width=30.0, height=100.0, depth=65.0, weight=10.0)
    )

    skuList.add(apple)
    skuList.add(banana)
    skuList.add(mango)

    val random = Random

    val workGroupNumber = random.nextInt(5)

    for (workGroupCount in 1..workGroupNumber+1) {
        val workGroupUid = "workgroup$workGroupCount"
        val skus: MutableList<SkuInfo> = mutableListOf()
        val skuNumber = random.nextInt(1,skuList.size)

        for (skuCount in 1..skuNumber+1) {
            val sku = skuList.random()
            sku.quantity = random.nextInt(1,15)
            skus.add(sku)
        }

        val workGroupInfo = WorkGroupInfo(workGroupUid=workGroupUid, skus=skus)
        workGroupList.add(workGroupInfo)
    }

    return workGroupList
}

fun createPicking(workGroup: WorkGroupInfo): Packing {
    val packer = Packing()

    for (sku in workGroup.skus) {
        for (index in 0..sku.quantity) {
            val name = sku.locationCode + "_" + sku.skuUid
            val item = Item(
                sku.skuUid,
                sku.locationCode,
                name,
                sku.cbmw.width,
                sku.cbmw.height,
                sku.cbmw.depth,
                sku.cbmw.weight,
                1
            )
            packer.addItem(item)
        }
    }

    packer.pack()

    return packer
}

fun createPickingGroups(packingResult: Packing, workGroupUid: String): MutableList<Picking> {
    var pickingGroup = mutableListOf<Picking>()

    for (tote in packingResult.totes) {
        var nameList = mutableListOf<String>()
        var skuInfo = mutableMapOf<String, String>()

        for (item in tote.items) {
            nameList.add(item.itemId)

            if (!skuInfo.containsKey(item.name)) {
                skuInfo[item.itemId] = item.location
            }
        }

        for (sku in skuInfo) {
            val quantity = nameList.count{it == sku.key}
            val pickingSku = PickingSku(skuUid = sku.key,
                locationCode = skuInfo[sku.key].toString(),
                quantity = quantity
            )
            val picking = Picking(workGroupUid, pickingSku)
            pickingGroup.add(picking)
        }
    }

    return pickingGroup
}
