package com.example.binpacking.domain

data class OutputRow(val workGroupId: String, val toteId: String, val skuId: String,
                     val width: String, val height: String, val depth: String, val weight: String,
                     val positionX: String, val positionY: String, val positionZ: String)

data class InputRow(val workGroupId: String, val skuId: String, val quantity: String, val locationCode: String,
                    val width: String, val height: String, val depth: String, val weight: String)

data class PerformanceOutput (var algorithm: String, var workGroup: String,
                              var totalToteCount: Int, var totalSkuCount: Int, var duration: Long)
