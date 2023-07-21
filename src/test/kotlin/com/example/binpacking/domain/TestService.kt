package com.example.binpacking.domain

import com.example.binpacking.entity.Item
import com.example.binpacking.service.PackingService
import kotlin.random.Random

class TestService {
    val workGroupList: List<WorkGroupInfo> = createWorkGroupList()
    val toteList: List<List<Picking>> = runningTest()

    private fun runningTest(): MutableList<List<Picking>> {
        val toteList: MutableList<List<Picking>> = mutableListOf()
        workGroupList.forEach { workGroup ->
            val packingResult = createPicking(workGroup)
            val totes = createPickingGroups(packingResult, workGroup.workGroupUid)

            totes.map { tote ->
                toteList.add(tote)
            }
        }

        return toteList
    }

    private fun testData(): List<SkuInfo> {
        val apple1 = SkuInfo(
            skuUid = "apple", quantity = 0, locationCode = "a-01-01",
            cbmw = CbmwInfo(width = 180.0, height = 100.0, depth = 80.0, weight = 30.0)
        )
        val apple2 = SkuInfo(
            skuUid = "apple", quantity = 0, locationCode = "a-01-02",
            cbmw = CbmwInfo(width = 180.0, height = 100.0, depth = 80.0, weight = 30.0)
        )

        val banana = SkuInfo(
            skuUid = "banana", quantity = 0, locationCode = "a-03-01",
            cbmw = CbmwInfo(width = 150.0, height = 120.0, depth = 50.0, weight = 50.0)
        )
        val mango = SkuInfo(
            skuUid = "mango", quantity = 0, locationCode = "a-02-02",
            cbmw = CbmwInfo(width = 200.0, height = 100.0, depth = 65.0, weight = 10.0)
        )

        return listOf(apple1, apple2, banana, mango)
    }

    private fun createWorkGroup(skuList: List<SkuInfo>, workGroupCount: Int): WorkGroupInfo {
        val workGroupUid = "workgroup$workGroupCount"
        val skus: MutableList<SkuInfo> = mutableListOf()
        val skuNumber = Random.nextInt(1, skuList.size)

        for (skuCount in 1 until skuNumber + 1) {
            val sku = skuList.random()
            sku.quantity = Random.nextInt(1, 10)
            skus.add(sku)
        }

        return WorkGroupInfo(workGroupUid, skus)
    }

    private fun createWorkGroupList(): List<WorkGroupInfo> {
        val skuList = testData()
        val workGroupList: MutableList<WorkGroupInfo> = mutableListOf()

        val workGroupNumber = Random.nextInt(1, 5)

        for (workGroupCount in 1 until workGroupNumber + 1) {
            val workGroupInfo = createWorkGroup(skuList, workGroupCount)
            workGroupList.add(workGroupInfo)
        }

        return workGroupList
    }

    private fun createPicking(workGroup: WorkGroupInfo): PackingService {
        val packer = PackingService()

        workGroup.skus.map { sku ->
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
                packer.packingItem.addItem(item)
            }
        }

        packer.pack()

        return packer
    }

    private fun createPickingGroups(packingResult: PackingService, workGroupUid: String): List<List<Picking>> {
        val totes: MutableList<List<Picking>> = mutableListOf()

        packingResult.packingTote.totes.map { tote ->
            val pickingGroup = mutableListOf<Picking>()
            val thisTote = mutableListOf<Picking>()
            val nameList = mutableListOf<String>()
            val skuInfo = mutableMapOf<String, List<String>>()

            tote.items.map { item ->
                nameList.add(item.name)

                if (!skuInfo.containsKey(item.name)) {
                    skuInfo[item.name] = arrayListOf(item.id, item.location)
                }
            }

            skuInfo.map { sku ->
                val quantity = nameList.count { name -> name == sku.key }
                val pickingSku = PickingSku(
                    skuUid = skuInfo[sku.key]!![0],
                    quantity = quantity
                )
                val picking = Picking(workGroupUid, pickingSku)
                pickingGroup.add(picking)
            }

            pickingGroup.map { picking -> thisTote.add(picking) }

            totes.add(thisTote)
        }

        return totes
    }
}