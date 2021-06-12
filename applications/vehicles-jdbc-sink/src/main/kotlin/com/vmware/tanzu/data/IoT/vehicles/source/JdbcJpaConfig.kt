package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.telemetry.repositories.VehicleTelemetryRepository
import org.hibernate.cfg.AvailableSettings
import org.hibernate.dialect.PostgreSQL9Dialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.hibernate5.SpringSessionContext
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@Configuration
@EnableJpaRepositories(basePackageClasses = [VehicleTelemetryRepository::class])
@EnableTransactionManagement
class JdbcJpaConfig {
    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean? {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        em.setPackagesToScan(Vehicle::class.java.packageName)
        em.setMappingResources(
            "hibernate/VehicleTelemetry.hbm.xml"
        )
        //these needed to be added to have all hibernate config done in one place.
        em.jpaPropertyMap[AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS] = SpringSessionContext::class.java.name
        em.jpaPropertyMap[AvailableSettings.DIALECT] = PostgreSQL9Dialect::class.java.name
        val vendor = HibernateJpaVendorAdapter()
        vendor.setShowSql(true)
        em.jpaVendorAdapter = vendor
        return em
    }
}