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
    mavenCentral()
    mavenLocal()
}

extra["springCloudVersion"] = "2021.0.0"
extra["springGeodeVersion"] = "1.4.3"



dependencies {
    implementation("com.google.code.gson:gson")
    implementation("org.springdoc:springdoc-openapi:2.2.0")
    implementation("com.github.nyla-solutions:nyla.solutions.core:1.4.4")
    implementation(project(":components:IoT-connected-vehicles-domains"))
    implementation(project(":components:vehicle-messaging"))
    implementation(project(":components:vehicle-messaging-mqtt"))
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0")
//    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-rabbit")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
}

dependencyManagement {
    imports {
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
