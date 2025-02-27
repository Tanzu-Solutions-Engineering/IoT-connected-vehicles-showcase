package iot.connected.vehicle.server;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import iot.connected.vehicle.server.repository.VehicleServerRepository;
import iot.connected.vehicle.server.repository.gemfire.VehicleGemFireRepository;
import org.apache.geode.cache.*;
import org.apache.geode.distributed.ServerLauncher;
import org.apache.geode.pdx.PdxSerializer;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Paths;

@Configuration
public class GemFireConfig {

    @Value("${gemfire.server.name:vehicle-server}")
    private String serverName;

//    @Value("${gemfire.start-locator:true}")
//    private String startLocator;


//    @Value("${gemfire.async.event.queue.id}")
//    private String asyncEventQueueId;

    @Value("${gemfire.pdx.patterns:.*}")
    private String pdxClassPatterns;

    @Value("${gemfire.server.port:40404}")
    private Integer serverPort;

    @Value("${gemfire.read.pdx.serialize:false}")
    private boolean readPdxSerialized;

    @Value("${gemfire.working.dir}")
    private String workingDirectory;

    @Value("${gemfire.pdx.disk.store:PDX_DS}")
    private String pdxDiskStore;

    @Value("${gemfire.statistic.archive.file:vehicle-server.gfs}")
    private String statisticArchiveFile;

    @Value("${gemfire.pdx.archive.disk.space.limit:5}")
    private String archiveDiskSpaceLimit;

    @Value("${gemfire.pdx.archive.file.size.limit:5}")
    private String archiveFileSizeLimit;

    @Value("${gemfire.partitioned.persisted.disk.store.name:partitionedPersistedDiskStoreName}")
    private String partitionedPersistedDiskStoreName;

    @Value("${gemfire.pdx.disk.store.name:PDX_STORE}")
    private String pdxDataStoreName;

    @Bean
    Cache cacheFactory(ServerLauncher launcher)
    {
        return CacheFactory.getAnyInstance();
    }

    @Bean
    PdxSerializer serializer()
    {
        return new ReflectionBasedAutoSerializer(pdxClassPatterns);
    }

    @Bean
    ServerLauncher builder(PdxSerializer pdxSerializer)
    {
        var serverLauncher = new ServerLauncher.Builder()
                .setWorkingDirectory(workingDirectory)
                .setMemberName(serverName)
                .setServerPort(serverPort)
                .set("start-locator","localhost[10334]")
//                .set("locators",locators)
                .set("statistic-sampling-enabled","true")
                .set("statistic-archive-file",workingDirectory+"/"+statisticArchiveFile)
                .set("archive-disk-space-limit",archiveDiskSpaceLimit)
                .set("archive-file-size-limit",archiveFileSizeLimit)
                .setPdxReadSerialized(readPdxSerialized)
                .setPdxDiskStore(pdxDiskStore)
                .setPdxSerializer(pdxSerializer)
                .build();

        serverLauncher.start();

        return serverLauncher;
    }

    @Bean
    Region<String, Vehicle> partitioned(Cache cache)
    {
        Region<String, Vehicle>  region  =  (Region)cache.createRegionFactory(RegionShortcut.PARTITION)
                .create("Vehicle");
        return region;
    }

    @Bean
    VehicleServerRepository repository(Region<String, Vehicle> vehicleRegion)
    {
        return new VehicleGemFireRepository(vehicleRegion);
    }


    @Bean
    DiskStoreFactory diskStoreFactory(Cache cache)
    {
        var factory = cache.createDiskStoreFactory();
        factory.setDiskDirs(new File[]{Paths.get(workingDirectory).toFile()});

        return factory;
    }

    @Bean
    DiskStore pdxDiskStoreNameDiskStore(Cache cache, DiskStoreFactory diskStoreFactory)
    {
        return diskStoreFactory.create(pdxDataStoreName);
    }

    @Bean
    DiskStore partitionedPersistedDiskStore(Cache cache, DiskStoreFactory diskStoreFactory)
    {
        return diskStoreFactory.create(partitionedPersistedDiskStoreName);
    }

    @Bean
    Region<String, Vehicle> vehicleRegion(Cache cache, @Qualifier("partitionedPersistedDiskStore") DiskStore diskStore)
    {
        Region<String, Vehicle>  region  =  (Region)cache.createRegionFactory(RegionShortcut.PARTITION_PERSISTENT)
                .setDiskStoreName(diskStore.getName())
                .create("Vehicle_persisted");
        return region;
    }

}
