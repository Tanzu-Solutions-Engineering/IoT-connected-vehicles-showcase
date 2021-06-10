package com.vmware.tanzu.data.schedule.jdbc

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.jdbc.core.JdbcTemplate

@ExtendWith(MockitoExtension::class)
internal class SqlSchedulerTest{

    @Mock
    private lateinit var  jdbcTemplate: JdbcTemplate;

    private val sql= "select * from table";

    @Test
    internal fun run() {
        var subject = SqlScheduler(sql,jdbcTemplate);
        subject.run();
        verify(jdbcTemplate).execute(sql);
    }
}