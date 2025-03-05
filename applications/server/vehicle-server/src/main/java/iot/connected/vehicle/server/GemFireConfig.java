package iot.connected.vehicle.server;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import iot.connected.vehicle.server.repository.VehicleServerRepository;
import iot.connected.vehicle.server.repository.gemfire.VehicleGemFireRepository;
import lombok.SneakyThrows;
import nyla.solutions.core.io.IO;
import org.apache.geode.cache.*;
import org.apache.geode.cache.wan.GatewayReceiver;
import org.apache.geode.cache.wan.GatewaySender;
import org.apache.geode.distributed.ServerLauncher;
import org.apache.geode.pdx.PdxSerializer;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Paths;

import static java.lang.System.setProperty;

@Configuration
public class GemFireConfig {

    private static final Logger log = LoggerFactory.getLogger(GemFireConfig.class);
    @Value("${gemfire.server.name:vehicle-server}")
    private String serverName;

    @Value("${gemfire.pdx.patterns:.*}")
    private String pdxClassPatterns;

    @Value("${gemfire.server.port:20200}")
    private Integer serverPort;

    @Value("${gemfire.read.pdx.serialize:false}")
    private boolean readPdxSerialized;

    @Value("${gemfire.working.dir}")
    private String workingDirectory;


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

    @Value("${gemfire.startLocators:localhost[20010]}")
    private String startLocators;

    /**
     * 	If set, automatically starts a locator in the current process when the member connects
     * 	to the cluster and stops the locator when the member disconnects.
     * To use, specify the locator with an optional address or host specification
     * and a required port number, in one of these formats:
     */
    @Value("${gemfire.remoteLocators:localhost[10334]}")
    private String remoteLocators;

    @Value("${gemfire.distributedSystemId:2}")
    private String distributedSystemId;

    @Value("${gemfire.sender.id:vehicle-sender}")
    private String senderId;

    @Value("${gemfire.gateway.diskStore:vehicle-sender}")
    private String gatewayDiskStore;

    @Value("${gemfire.gateway.sender.name:vehicle-sender}")
    private String gatewaySenderName;

    @Value("${gemfire.gateway.remote.distributed.id:1}")
    private int remoteDistributedId;

    @Value("${gemfire.jmx.manager.port:20199}")
    private String jmxManagerPort;

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

    @SneakyThrows
    @Bean
    ServerLauncher builder(PdxSerializer pdxSerializer)
    {
        log.info("********************* startLocators: {}",startLocators);

        IO.mkdir(workingDirectory);
        setProperty("gemfire.remote-locators",remoteLocators);
        setProperty("gemfire.GatewaySender.REMOVE_FROM_QUEUE_ON_EXCEPTION","false");
        setProperty("jmx-manager-port",jmxManagerPort);

        var serverLauncher = new ServerLauncher.Builder()
                .setWorkingDirectory(workingDirectory)
                .setMemberName(serverName)
                .setServerPort(serverPort)
                .setPdxDiskStore(pdxDataStoreName)
                .set("remote-locators",remoteLocators)
                .set("statistic-sampling-enabled","true")
                .set("statistic-archive-file",workingDirectory+"/"+statisticArchiveFile)
                .set("archive-disk-space-limit",archiveDiskSpaceLimit)
                .set("archive-file-size-limit",archiveFileSizeLimit)
                .set("distributed-system-id", distributedSystemId)
                .set("bind-address","127.0.0.1")
                .setPdxReadSerialized(readPdxSerialized)
                .setPdxSerializer(pdxSerializer)
                .setPdxPersistent(true)
                .set("start-locator",startLocators)
//                .set("locators",startLocators)
                .build();

        serverLauncher.start();

        return serverLauncher;
    }


    @Bean
    VehicleServerRepository repository(Region<String, Vehicle> vehicleRegion)
    {
        return new VehicleGemFireRepository(vehicleRegion);
    }

    @SneakyThrows
    GatewayReceiver receiver(Cache cache){
        var receiver = cache.createGatewayReceiverFactory().create();

        receiver.start();
        return receiver;
    }

    @Bean
    GatewaySender sender(Cache cache, @Qualifier("gatewayDiskStore") DiskStore gateDS){
        var sender = cache.createGatewaySenderFactory()
                .setDiskStoreName(gateDS.getName())
                .setPersistenceEnabled(true)
                .setParallel(true)
                .setMaximumQueueMemory(100)
                .create(gatewaySenderName,remoteDistributedId);

        sender.start();

        return sender;
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
    DiskStore gatewayDiskStore(Cache cache,DiskStoreFactory diskStoreFactory)
    {
        return diskStoreFactory.create(gatewayDiskStore);
    }

    @Bean
    DiskStore gatewayDiskStoreNameDiskStore(Cache cache, DiskStoreFactory diskStoreFactory)
    {
        return diskStoreFactory.create(gatewayDiskStore);
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
                .addGatewaySenderId(gatewaySenderName)
                .create("Vehicle");
        return region;
    }

}
