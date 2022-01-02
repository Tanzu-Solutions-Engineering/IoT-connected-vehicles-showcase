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

dependencies {
    implementation("org.springdoc:springdoc-openapi-ui:1.6.3")
    implementation("com.github.nyla-solutions:nyla.solutions.core:1.4.4")
    implementation(project(":components:IoT-connected-vehicles-domains"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(project(":components:vehicle-messaging"))
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0")
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
