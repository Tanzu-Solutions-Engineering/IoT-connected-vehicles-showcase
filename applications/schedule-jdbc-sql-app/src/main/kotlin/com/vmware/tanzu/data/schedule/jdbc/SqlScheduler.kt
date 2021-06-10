package com.vmware.tanzu.data.schedule.jdbc

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author Gregory Green
 */
@Component
class SqlScheduler(@Value("\${SqlScheduler.sql}") private val sql: String,
                   private val jdbcTemplate: JdbcTemplate) : Runnable {

    private val logger = LoggerFactory.getLogger(this.javaClass);

    @Value("\${SqlScheduler.fixedDelayString.milliseconds}")
    private  var fixedDelay : Long =0 ;
    /**
     * When an object implementing interface `Runnable` is used
     * to create a thread, starting the thread causes the object's
     * `run` method to be called in that separately executing
     * thread.
     *
     *
     * The general contract of the method `run` is that it may
     * take any action whatsoever.
     *
     * @see java.lang.Thread.run
     */
    @Scheduled(fixedDelayString = "\${SqlScheduler.fixedDelayString.milliseconds}")
    override fun run() {
        logger.info("Scheduled fixedDelay {} milliseconds--- Executing {}",fixedDelay,sql);
        jdbcTemplate.execute(sql);
    }
}