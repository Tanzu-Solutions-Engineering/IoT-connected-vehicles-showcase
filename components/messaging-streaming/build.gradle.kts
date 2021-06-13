import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
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
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//    implementation("org.springframework.amqp:spring-rabbit:2.3.5")
    implementation(project(":components:vehicle-messaging"))
    implementation("com.rabbitmq:stream-client:0.1.0-SNAPSHOT")
    implementation("org.apache.qpid:proton-j:0.33.8")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.0-M1")
    testImplementation("org.mockito:mockito-core:3.8.0")
    testImplementation("org.mockito:mockito-junit-jupiter:3.8.0")
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
