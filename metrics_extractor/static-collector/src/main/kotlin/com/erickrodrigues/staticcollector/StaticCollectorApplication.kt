package com.erickrodrigues.staticcollector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StaticCollectorApplication

fun main(args: Array<String>) {
    runApplication<StaticCollectorApplication>(*args)
}
