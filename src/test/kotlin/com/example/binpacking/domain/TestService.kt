package com.example.binpacking.domain

import com.example.binpacking.entity.Item
import com.example.binpacking.service.Algorithm
import com.example.binpacking.service.PackingService
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.random.Random

class TestService {
    val workGroupList: List<WorkGroupInfo> = createWorkGroupList()

    fun runningTest() {
        val toteList: MutableList<List<Picking>> = mutableListOf()

        workGroupList.forEach { workGroup ->
            val packingResult = createPicking(workGroup)
            outputDataToCsv(filePath = "./src/main/files/bin-packing_output.csv",
                workGroupUid = workGroup.workGroupUid,
                packingTotes = packingResult.singleItemPackingTote)
        }
    }

    private fun inputFromCsvData(fileUrl: String): MutableMap<String, MutableList<SkuInfo>> {
        val sampleDataFile = File(fileUrl)
        val reader = BufferedReader(FileReader(sampleDataFile, Charsets.UTF_8))

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

    private fun inputDataToCsv(inputData: List<WorkGroupInfo>, filePath: String) {
        var inputRows: MutableList<InputRow> = mutableListOf()

        inputData.forEach { workGroup ->
            val workGroupUid = workGroup.workGroupUid
            workGroup.skus.forEach { sku ->
                val row = InputRow(
                    workGroupUid,
                    sku.skuUid,
                    sku.quantity.toString(),
                    sku.locationCode,
                    sku.cbmw.width.toString(),
                    sku.cbmw.height.toString(),
                    sku.cbmw.depth.toString(),
                    sku.cbmw.weight.toString()
                )

                inputRows.add(row)
            }
        }

        FileWriter(filePath, true).use { writer ->
            inputRows.forEach { row -> writer.append(
                "${row.workGroupId},${row.skuId},${row.quantity},${row.locationCode}" +
                        ",${row.width},${row.height},${row.depth},${row.weight}\n"
            ) }
        }
    }

    private fun outputDataToCsv(filePath: String, workGroupUid: String, packingTotes: PackingService.PackingTote) {
        var outputRows: MutableList<OutputRow> = mutableListOf()

        packingTotes.totes.forEach{ tote ->
            tote.items.forEach { item ->

                val row = OutputRow(
                    workGroupId = workGroupUid,
                    toteId = tote.name,
                    skuId = item.skuId,
                    width = item.width.toString(),
                    height = item.height.toString(),
                    depth = item.depth.toString(),
                    weight = item.weight.toString(),
                    positionX = item.position[0].toString(),
                    positionY = item.position[1].toString(),
                    positionZ = item.position[2].toString()
                )

                outputRows.add(row)
            }
        }

        FileWriter(filePath, true).use { writer ->
            outputRows.forEach { row -> writer.append(
                "${row.workGroupId},${row.toteId},${row.skuId}," +
                        "${row.width},${row.height},${row.depth}," +
                        "${row.positionX},${row.positionY},${row.positionZ}\n"
            ) }
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
            val sku = skuList.random() // item 카테고리 리스트 중에 어떤 item으로 workGroup 만들지 랜덤 뽑기
            sku.quantity = Random.nextInt(10, 15) // 해당 item 몇 개 만들지 보기
            skus.add(sku)
        }

        return WorkGroupInfo(workGroupUid, skus)
    }

    private fun createWorkGroupList(): List<WorkGroupInfo> {
        val workGroupList: MutableList<WorkGroupInfo> = mutableListOf()
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
                skuId = sku.skuUid,
                location = sku.locationCode,
                name = name,
                length1 = sku.cbmw.width,
                length2 = sku.cbmw.height,
                length3 = sku.cbmw.depth,
                weight = sku.cbmw.weight,
                quantity = sku.quantity,
                workId = ""
            )
            item.getDimension()
            packer.packingItem.addItem(item)

        }

        //packer.pack(algorithm = Algorithm.FFD)
        packer.packForTest(algorithm = Algorithm.FFD)

        return packer
    }

    private fun createPickingFloor(packing: PackingService) {
        packing.packingTote.totes.forEach { tote ->
            println("===================== [" + tote.name + "] =====================")
            println("total "+tote.items.size+" items")
            tote.items.forEach { item ->
                println(item.skuId + " / " + item.position+ " + " + listOf(item.width, item.depth, item.height))
            }
            println()
        }
    }
}
