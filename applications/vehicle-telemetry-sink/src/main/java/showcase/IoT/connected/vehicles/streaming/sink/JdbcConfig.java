package showcase.IoT.connected.vehicles.streaming.sink;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import showcase.IoT.connected.vehicles.streaming.sink.repository.VehicleTelemetryRepository;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackageClasses = VehicleTelemetryRepository.class)
@EnableTransactionManagement
public class JdbcConfig {

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        var em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource);
        em.setPackagesToScan(Vehicle.class.getName());

        em.setMappingResources(
                "hibernate/VehicleTelemetry.hbm.xml"
        );

        //these needed to be added to have all hibernate config done in one place.
        em.setJpaPropertyMap(Map.of(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, SpringSessionContext.class));

        var vendor = new HibernateJpaVendorAdapter();
        vendor.setShowSql(true);
        em.setJpaVendorAdapter(vendor);
        return em;
    }
}
