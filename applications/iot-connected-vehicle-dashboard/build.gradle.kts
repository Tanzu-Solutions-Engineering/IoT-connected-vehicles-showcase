import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "com.vmware.tanzu.data"
version = "0.0.3-SNAPSHOT"
java {
	sourceCompatibility = JavaVersion.VERSION_17
}


repositories {
	mavenCentral()
	mavenLocal()
}

extra["springCloudVersion"] = "2021.0.0"
extra["springGeodeVersion"] = "1.6.3"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("io.pivotal.services.dataTx:dataTx-geode-extensions-core:2.4.0")
	implementation("io.pivotal.services.dataTx:dataTx-geode-extensions-spring-security:2.5.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.github.nyla-solutions:nyla.solutions.core:1.4.4")
	implementation(project(":components:IoT-connected-vehicles-domains"))
	implementation(project(":components:iot-connected-vehicles-repository"))
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
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

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}
dependencyManagement {
	imports {
		mavenBom("org.springframework.geode:spring-geode-bom:${property("springGeodeVersion")}")
	}
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}


tasks.withType<Test> {
	useJUnitPlatform()
}
