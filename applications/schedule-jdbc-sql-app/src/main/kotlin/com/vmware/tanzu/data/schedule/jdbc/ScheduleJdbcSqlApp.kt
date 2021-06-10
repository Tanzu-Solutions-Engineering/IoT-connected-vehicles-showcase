package com.vmware.tanzu.data.schedule.jdbc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScheduleJdbcSqlApp

fun main(args: Array<String>) {
	runApplication<ScheduleJdbcSqlApp>(*args)
}