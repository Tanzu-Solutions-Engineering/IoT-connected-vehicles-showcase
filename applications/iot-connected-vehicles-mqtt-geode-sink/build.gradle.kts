import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    //maven
}

group = "com.vmware.tanzu.data"
version = "0.0.1-SNAPSHOT"
java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://packagecloud.io/rabbitmq/maven-milestones/maven2") }
    mavenCentral()
    mavenLocal()
}

extra["springCloudVersion"] = "2021.0.0"
extra["springGeodeVersion"] = "1.4.3"



dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.geode:spring-geode-starter")
    {
        exclude(group = "org.apache.geode")
    }
    //------------
    //Apache Geode
    implementation("org.apache.geode:geode-core:1.13.7")
    implementation("org.apache.geode:geode-cq:1.13.7")
    implementation("org.apache.geode:geode-lucene:1.13.7")
    implementation("org.apache.geode:geode-wan:1.13.7")
    implementation("org.apache.geode:geode-common:1.13.7")
    implementation("org.apache.geode:geode-management:1.13.7")
    implementation("org.apache.geode:geode-logging:1.13.7")
    implementation("org.apache.geode:geode-membership:1.13.7")
    implementation("org.apache.geode:geode-unsafe:1.13.7")
    implementation("org.apache.geode:geode-serialization:1.13.7")

    implementation(project(":components:iot-connected-vehicles-repository"))
    implementation(project(":components:IoT-connected-vehicles-domains"))
    implementation(project(":components:iot-connected-vehicles-repository-sink"))
    implementation(project(":components:vehicle-messaging"))
    implementation(project(":components:vehicle-messaging-mqtt"))
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.geode:spring-geode-bom:${property("springGeodeVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
