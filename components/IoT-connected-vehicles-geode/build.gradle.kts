plugins {
    kotlin("jvm") version "1.4.31"
}

group = "com.vmware.tanzu.data"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.apache.geode:geode-core:1.13.1");
    implementation("org.apache.geode:geode-lucene:1.13.1")
    implementation("org.junit.jupiter:junit-jupiter:5.4.2");
    implementation("org.apache.logging.log4j:log4j-api:2.13.1");
    testImplementation("org.mockito:mockito-junit-jupiter:3.8.0");
    testImplementation( "org.mockito:mockito-core:3.8.0")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
