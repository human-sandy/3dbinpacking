package com.example.binpacking.domain

import com.example.binpacking.entity.Item
import com.example.binpacking.service.PackingService
import kotlin.random.Random

class TestService {
    val workGroupList: List<WorkGroupInfo> = createWorkGroupList()

    fun runningTest() {
        val toteList: MutableList<List<Picking>> = mutableListOf()
        workGroupList.forEach { workGroup ->
            val packingResult = createPicking(workGroup)
            createPickingFloor(packingResult)
        }
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

    private fun testbedData(): List<SkuInfo> {
        val household1 = SkuInfo(
            skuUid = "household", quantity = 0, locationCode = "D2-31-03",
            cbmw = CbmwInfo(width = 200.0, depth = 160.0, height = 130.0, weight = 0.0)
        )

        val household2 = SkuInfo(
            skuUid = "household", quantity = 0, locationCode = "A2-14-03",
            cbmw = CbmwInfo(width = 200.0, depth = 160.0, height = 130.0, weight = 0.0)
        )

        val beauty = SkuInfo(
            skuUid = "beauty", quantity = 0, locationCode = "B1-61-01",
            cbmw = CbmwInfo(width = 210.0, depth = 155.0, height = 100.0, weight = 0.0)
        )

        return listOf(household1, household2, beauty)
    }

    private fun createWorkGroup(skuList: List<SkuInfo>, workGroupCount: Int): WorkGroupInfo {
        val workGroupUid = "workgroup$workGroupCount"
        val skus: MutableList<SkuInfo> = mutableListOf()
        val skuNumber = Random.nextInt(1, skuList.size)

        for (skuCount in 1 until skuNumber + 1) {
            val sku = skuList.random()
            sku.quantity = Random.nextInt(10, 15)
            skus.add(sku)
        }

        return WorkGroupInfo(workGroupUid, skus)
    }

    private fun createWorkGroupList(): List<WorkGroupInfo> {
        val skuList = testbedData()
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
            val name = sku.locationCode + "_" + sku.skuUid
            val item = Item(
                sku.skuUid,
                sku.locationCode,
                name,
                sku.cbmw.width,
                sku.cbmw.height,
                sku.cbmw.depth,
                sku.cbmw.weight,
                sku.quantity
            )
            packer.packingItem.addItem(item)

        }

        packer.pack()

        return packer
    }

    private fun createPickingFloor(packing: PackingService) {
        packing.packingTote.totes.forEach { tote ->
            println("===================== [" + tote.name + "] =====================")
            tote.items.forEach { item ->
                println(item.id + " / " + item.quantity)
            }
            println()
        }
    }

}
