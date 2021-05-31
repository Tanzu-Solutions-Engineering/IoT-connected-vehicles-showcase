import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
}

group = "com.vmware.tanzu.data"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    mavenLocal()
}

extra["springCloudVersion"] = "2020.0.1"

dependencies {
    implementation("org.springframework:spring-context:5.3.1")
    implementation("org.springframework.data:spring-data-commons:2.4.5")
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")
    implementation("org.apache.logging.log4j:log4j-api:2.13.1")
    implementation(project(":components:IoT-connected-vehicles-domains"))
    implementation("com.github.nyla-solutions:nyla.solutions.core:1.4.3")
    testImplementation("org.mockito:mockito-junit-jupiter:3.8.0")
    testImplementation( "org.mockito:mockito-core:3.8.0")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
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
