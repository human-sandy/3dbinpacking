package com.example.binpacking.domain

import com.example.binpacking.entity.Item
import com.example.binpacking.service.Algorithm
import com.example.binpacking.service.PackingService
import com.example.binpacking.setToDecimal
import com.example.binpacking.entity.DEFAULT_NUMBER_OF_DECIMALS
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.time.measureTimedValue

class TestService {
    val workGroupList: List<WorkGroupInfo> = createWorkGroupList()
    val algorithm = listOf(Algorithm.FFD, Algorithm.BFD, Algorithm.MFK)
    // val algorithm:List<Algorithm> = listOf(Algorithm.BFD) or listOf(Algorithm.MFK) or enumValues<Algorithm>().toList()
    // 3개의 알고리즘을 동시에 돌리는 것은 테스트를 위한 csv 파일을 한 번에 뽑기 위함입니다.
    // 서버에 전달해줄 때에는 for문 때문에 toteList가 업데이트 되어서 마지막 알고리즘 값만 전달될 거에요.
    // 로봇에게 sku별 quantity를 제공해줄 때에는 하나의 알고리즘만 들어있는 리스트를 생성하고 packForTest() 대신 pack()을 실행하면 됩니다.

    @OptIn(kotlin.time.ExperimentalTime::class)
    fun runningTest() {
        val toteList: MutableList<List<Picking>> = mutableListOf()
        var packingResult: PackingService
        val performanceList: MutableList<PerformanceOutput> = mutableListOf()

        algorithm.forEach { algorithmType ->
            workGroupList.forEach { workGroup ->
                val measuredTime = measureTimedValue {
                    packingResult = createPicking(workGroup, algorithmType)
                }
                outputDataToCsv(
                    filePath = "./src/main/files/${algorithmType}_output.csv",
                    workGroupUid = workGroup.workGroupUid,
                    packingTotes = packingResult.singleItemPackingTote
                )
                performance(workGroup.workGroupUid, packingResult.singleItemPackingTote, algorithmType)

                val toteCount = packingResult.packingTote.totes.size

                var skuCount : Int = 0
                packingResult.packingTote.totes.forEach { tote ->
                    skuCount += tote.items.size
                }

                performanceList.add(
                    PerformanceOutput(
                        algorithm = algorithmType.toString(),
                        workGroup = workGroup.workGroupUid,
                        totalToteCount = toteCount,
                        totalSkuCount = skuCount,
                        duration = TimeUnit.NANOSECONDS.toMillis(measuredTime.duration.inWholeNanoseconds)
                ))
            }
        }

        measurePerformance(
            filePath = "./src/main/files/checkPerformance.csv",
            result = performanceList
        )
    }

    private fun inputFromCsvData(fileUrl: String): MutableMap<String, MutableList<SkuInfo>> {
        val sampleDataFile = File(fileUrl)
        val reader = BufferedReader(FileReader(sampleDataFile, Charsets.UTF_8))

        val orderList = mutableMapOf<String, MutableList<SkuInfo>>()

        reader.lines().forEach { row ->
            val data = row.split(",")
            val workGroupUid = data[0].replace("\uFEFF", "")
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
            inputRows.forEach { row ->
                writer.append(
                    "${row.workGroupId},${row.skuId},${row.quantity},${row.locationCode}" +
                            ",${row.width},${row.height},${row.depth},${row.weight}\n"
                )
            }
        }
    }

    private fun outputDataToCsv(filePath: String, workGroupUid: String, packingTotes: PackingService.PackingTote) {
        var outputRows: MutableList<OutputRow> = mutableListOf()

        packingTotes.totes.forEach { tote ->
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

        FileWriter(filePath, false).use { writer ->
            outputRows.forEach { row ->
                writer.append(
                    "${row.workGroupId},${row.toteId},${row.skuId}," +
                            "${row.width},${row.depth},${row.height}," +
                            "${row.positionX},${row.positionY},${row.positionZ}\n"
                )
            }
        }
    }

    private fun measurePerformance(filePath: String, result: MutableList<PerformanceOutput>) {
        FileWriter(filePath, true).use { writer ->
            result.forEach { output ->
                writer.append(
                    "${output.algorithm},${output.workGroup},${output.totalToteCount}," +
                            "${output.totalSkuCount},${output.duration}\n"
                )
            }
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

        val orderList= inputFromCsvData(fileUrl = "./src/main/files/binpacking_test.csv")

        orderList.map { workGroup ->
            val workGroupInfo = WorkGroupInfo(
                workGroupUid = workGroup.key.toString(),
                skus = workGroup.value
            )
            workGroupList.add(workGroupInfo)
        }

        /*val skuList = testbedData()
        val workGroupNumber = Random.nextInt(1, 5)

        for (workGroupCount in 1 until workGroupNumber + 1) {
            val workGroupInfo = createWorkGroup(skuList, workGroupCount)
            workGroupList.add(workGroupInfo)
        }*/

        return workGroupList
    }

    private fun createPicking(workGroup: WorkGroupInfo, algorithmType: Algorithm): PackingService {
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

        packer.packForTest(algorithm = algorithmType)

        return packer
    }

    private fun createPickingFloor(packing: PackingService) {
        packing.packingTote.totes.forEach { tote ->
            println("===================== [" + tote.name + "] =====================")
            println("total " + tote.items.size + " items")
            tote.items.forEach { item ->
                println(item.skuId + " / " + item.position + " + " + listOf(item.width, item.depth, item.height))
            }
        }
    }

    private fun performance(workGroupUid: String, packingTotes: PackingService.PackingTote, algorithmType: Algorithm) {

        println("<< $algorithmType >>")
        println("Total number of totes for $workGroupUid: ${packingTotes.totes.size}\n")
        var loadFactorSum: Double = 0.0
        var itemNumSum = 0
        packingTotes.totes.forEach { tote ->
            println("===================== [" + tote.name + "] =====================")
            val loadFactor = setToDecimal(
                (1 - (tote.availSpace / (tote.width * tote.depth * tote.height))) * 100,
                DEFAULT_NUMBER_OF_DECIMALS
            )
            println("Load factor: $loadFactor %")
            println("Number of packed items: ${tote.items.size}\n")
            loadFactorSum += loadFactor
            itemNumSum += tote.items.size
        }
        loadFactorSum /= packingTotes.totes.size
        itemNumSum /= packingTotes.totes.size
        println("Average load factor: $loadFactorSum %")
        println("Average number of packed items: ${itemNumSum}\n")
    }

//    inline fun <T> measureTimeValue(block: () -> T): TimedValue<T> {
//        contract {
//            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//        }
//
//        return TimeSource.Monotonic.measureTimedValue(block)
//    }
}
