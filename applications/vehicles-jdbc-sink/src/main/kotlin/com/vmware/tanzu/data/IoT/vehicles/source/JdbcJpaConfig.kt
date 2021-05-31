package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.domains.GpsLocation
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EnableJpaRepositories(basePackageClasses = [VehicleRepository::class])
@EnableTransactionManagement
@EntityScan(basePackageClasses = [Vehicle::class,GpsLocation::class])
class JdbcJpaConfig {

//
//    @PersistenceContext(unitName = "Vehicles")
//    protected var em: EntityManager? = null
}