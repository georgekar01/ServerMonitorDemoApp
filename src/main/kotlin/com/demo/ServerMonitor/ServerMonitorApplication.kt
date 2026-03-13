package com.demo.ServerMonitor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
class ServerMonitorApplication

fun main(args: Array<String>) {
	runApplication<ServerMonitorApplication>(*args)
}
