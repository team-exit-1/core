package team.exit_1.backend.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
class CoreApplication

fun main(args: Array<String>) {
	runApplication<CoreApplication>(*args)
}
