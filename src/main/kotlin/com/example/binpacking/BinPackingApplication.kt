package com.example.binpacking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BinPackingApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<BinPackingApplication>(*args)
        }
    }
}