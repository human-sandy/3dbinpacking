package com.example.binpacking.domain

import com.example.binpacking.entity.Item
import com.example.binpacking.service.PackingService
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.Dictionary
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

    // order_id,sku_id,request_quantity,location_code,
    // width,height,depth,weight
    private fun csvData(fileUrl: String): MutableMap<String, MutableList<SkuInfo>> {
        val sampleDataFile = File(fileUrl)
        val reader = BufferedReader(FileReader(sampleDataFile, Charsets.UTF_8))

        //Dictionary<Int, List<SkuInfo>>
        val orderList = mutableMapOf<String, MutableList<SkuInfo>>()

        reader.lines().forEach {row ->
            val data = row.split(",")
            val workGroupUid = data[0]
            val cbmw = CbmwInfo(
                width = data[4].toDouble(),
                height = data[5].toDouble(),
                depth = data[6].toDouble(),
                weight = data[7].toDouble()
            )
            val sku = SkuInfo(
                skuUid = data[1],
                quantity = data[2].toInt(),
                locationCode = data[3],
                cbmw = cbmw
            )
            if (!orderList.containsKey(workGroupUid))
                orderList[workGroupUid] = mutableListOf(sku)
            else
                orderList[workGroupUid]?.add(sku)
        }

        return orderList
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
            val sku = skuList.random() // item 카테고리 리스트 중에 어떤 item으로 workGroup 만들지 랜덤 뽑기
            sku.quantity = Random.nextInt(10, 15) // 해당 item 몇 개 만들지 보기
            skus.add(sku)
        }

        return WorkGroupInfo(workGroupUid, skus)
    }

    private fun createWorkGroupList(): List<WorkGroupInfo> {
        val workGroupList: MutableList<WorkGroupInfo> = mutableListOf()
        /* val orderList = csvData(fileUrl = "./src/main/files/sample_0803.csv")

        orderList.map { workGroup ->
            val workGroupInfo = WorkGroupInfo(
                workGroupUid = workGroup.key,
                skus = workGroup.value
            )

            workGroupList.add(workGroupInfo)
        }*/

        val skuList = testbedData()
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
            item.getDimension()
            packer.packingItem.addItem(item)

        }

        packer.pack()

        return packer
    }

    private fun createPickingFloor(packing: PackingService) {
        packing.packingTote.totes.forEach { tote ->
            println("===================== [" + tote.name + "] =====================")
            println("total "+tote.items.size+" items")
            tote.items.forEach { item ->
                println(item.id + " / " + item.position+ " + " + listOf(item.width, item.depth, item.height))
            }
            println()
        }
    }
}
