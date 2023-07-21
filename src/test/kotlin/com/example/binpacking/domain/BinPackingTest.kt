package com.example.binpacking.domain

import org.junit.jupiter.api.Test


class BinPackingTest {

    @Test
    fun testBinPacking() {
        var index = 0
        val testService = TestService()

        testService.workGroupList.map { workGroup ->
            println("=============== ${workGroup.workGroupUid} ===============")
            workGroup.skus.forEach { work ->
                println("[" + work.skuUid + "] " + work.locationCode + " / " + work.quantity)
            }
            println()
        }

        testService.toteList.map { totes ->
            index++
            println("================= Tote $index =================")
            totes.forEach { tote ->
                println("[" + tote.workGroupUid + "] " + tote.pickingSku.skuUid + " : " + tote.pickingSku.quantity)
            }
            println()
        }
    }
}