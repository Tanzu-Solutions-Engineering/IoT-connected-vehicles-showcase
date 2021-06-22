import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    maven
}

group = "com.vmware.tanzu.data"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://packagecloud.io/rabbitmq/maven-milestones/maven2") }
    mavenCentral()
    mavenLocal()
}

extra["springCloudVersion"] = "2020.0.1"
extra["springGeodeVersion"] = "1.4.3"



dependencies {
    implementation("org.springdoc:springdoc-openapi-ui:1.5.2")
    implementation("com.github.nyla-solutions:nyla.solutions.core:1.4.3")
    implementation(project(":components:IoT-connected-vehicles-domains"))
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation(project(":components:vehicle-messaging"))
    implementation(project(":components:vehicle-messaging-mqtt"))
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }

    imports {
        mavenBom("org.springframework.geode:spring-geode-bom:${property("springGeodeVersion")}")
    }
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
