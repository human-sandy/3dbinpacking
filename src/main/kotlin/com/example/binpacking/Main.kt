package com.example.binpacking

import com.example.binpacking.domain.*
import kotlin.random.Random

fun main(args: Array<String>) {
    val workGroupList: MutableList<WorkGroupInfo> = createWorkGroup()

    val toteList: MutableList<MutableList<Picking>> = mutableListOf()
    var index: Int = 0

    for (workGroup in workGroupList) {
        val packingResult = createPicking(workGroup)
        val totes = createPickingGroups(packingResult, workGroup.workGroupUid)

        for (tote in totes) {
            toteList.add(tote)
        }
    }

    workGroupList.forEach { workGroup ->
        println("=============== ${workGroup.workGroupUid} ===============")
        for (work in workGroup.skus) {
            println("[" + work.skuUid + "] " + work.quantity)
        }
        println()
    }

    println()

    toteList.forEach { totes ->
        index++
        println("=============== Tote $index ===============")
        for (tote in totes) {
            println("[" + tote.workGroupUid + "] " + tote.pickingSku.skuUid + " : " + tote.pickingSku.quantity)
        }
        println()
    }
}

fun createWorkGroup(): MutableList<WorkGroupInfo> {
    var workGroupList : MutableList<WorkGroupInfo> = mutableListOf()
    var skuList : MutableList<SkuInfo> = mutableListOf()

    val apple = SkuInfo(skuUid = "apple", quantity = 0, locationCode = "a-01-01",
        cbmw = CbmwInfo(width=180.0, height=100.0, depth=80.0, weight=30.0)
    )
    val banana = SkuInfo(skuUid = "banana", quantity = 0, locationCode = "a-03-01",
        cbmw = CbmwInfo(width=150.0, height=120.0, depth=80.0, weight=50.0)
    )
    val mango = SkuInfo(skuUid = "mango", quantity = 0, locationCode = "a-02-02",
        cbmw = CbmwInfo(width=200.0, height=100.0, depth=65.0, weight=10.0)
    )

    skuList.apply {
        add(apple)
        add(banana)
        add(mango)
    }

    val random = Random

    val workGroupNumber = random.nextInt(1,5)

    for (workGroupCount in 1 until workGroupNumber+1) {
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

    workGroup.skus.map {sku ->
        for (index in 0 until sku.quantity) {
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

fun createPickingGroups(packingResult: Packing, workGroupUid: String): MutableList<MutableList<Picking>> {
    val totes : MutableList<MutableList<Picking>> = mutableListOf()

    for (tote in packingResult.totes) {
        val pickingGroup = mutableListOf<Picking>()
        val thisTote = mutableListOf<Picking>()
        val nameList = mutableListOf<String>()
        val skuInfo = mutableMapOf<String, String>()

        tote.items.map { item ->
            nameList.add(item.itemId)

            if (!skuInfo.containsKey(item.name)) {
                skuInfo[item.itemId] = item.location
            }
        }

        skuInfo.map {sku ->
            val quantity = nameList.count{name -> name == sku.key}
            val pickingSku = PickingSku(
                skuUid = sku.key,
                locationCode = skuInfo[sku.key].toString(),
                quantity = quantity)
            val picking = Picking(workGroupUid, pickingSku)
            pickingGroup.add(picking)
        }

        pickingGroup.map { picking -> thisTote.add(picking) }

        totes.add(thisTote)
    }

    return totes
}
