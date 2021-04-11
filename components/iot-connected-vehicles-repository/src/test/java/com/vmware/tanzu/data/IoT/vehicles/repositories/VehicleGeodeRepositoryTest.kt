//package com.vmware.tanzu.data.IoT.vehicles.repositories
//
//import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
//import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
//import org.apache.geode.cache.Region
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.ArgumentMatchers.any
//import org.mockito.ArgumentMatchers.anyString
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.junit.jupiter.MockitoExtension
//
//@ExtendWith(MockitoExtension::class)
//internal class VehicleGeodeRepositoryTest() {
//    @Mock
//    private lateinit var region: Region<String, Vehicle>;
//    private lateinit var expected: Vehicle
//    private lateinit var subject: VehicleGeodeRepository;
//
//    @BeforeEach
//    internal fun setUp() {
//        expected = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();
//        subject = VehicleGeodeRepository(region);
//    }
//
//    @Test
//    internal fun save() {
//        val actual = subject.save(expected);
//        Mockito.verify(region).put(anyString(),any());
//        assertEquals(expected,actual);
//    }
//}