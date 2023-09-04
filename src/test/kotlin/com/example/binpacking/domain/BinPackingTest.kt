package com.example.binpacking.domain

import org.junit.jupiter.api.Test


class BinPackingTest {

    @Test
    fun testBinPacking() {
        var index = 0
        val testService = TestService()

        testService.workGroupList.map { workGroup ->
            println("=============== Workgroup: ${workGroup.workGroupUid} ===============")
            workGroup.skus.forEach { work ->
                println("[" + work.skuUid + "] " + work.locationCode + " / " + work.quantity)
            }
            println()
        }

        testService.runningTest()
    }
}
