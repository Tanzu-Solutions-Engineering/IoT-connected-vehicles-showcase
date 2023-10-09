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



dependencies {
//    implementation("org.springdoc:springdoc-openapi-ui:1.5.2")
    implementation("org.springdoc:springdoc-openapi:2.2.0")
    implementation("com.github.nyla-solutions:nyla.solutions.core:1.4.4")
    implementation(project(":components:IoT-connected-vehicles-domains"))
    implementation(project(":components:vehicle-messaging"))
    implementation(project(":components:messaging-streaming"))
    implementation(project(":components:vehicle-messaging-streaming"))
//    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("com.rabbitmq:stream-client:0.3.0")
    implementation("org.apache.qpid:proton-j:0.33.8")
    implementation("org.xerial.snappy:snappy-java:1.1.8.4")
    implementation("org.lz4:lz4-java:1.8.0")
    implementation("com.github.luben:zstd-jni:1.5.0-2")
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
