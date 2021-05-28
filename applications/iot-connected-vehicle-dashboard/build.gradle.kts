import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.30"
	kotlin("plugin.spring") version "1.4.30"
}

group = "com.vmware.tanzu.data"
version = "0.0.2-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	mavenLocal()
}

extra["springGeodeVersion"] = "1.4.3"

dependencies {
//	implementation("io.pivotal.services.dataTx:dataTx-geode-extensions-core:2.4.0")

	implementation("io.pivotal.services.dataTx:dataTx-geode-extensions-core:2.4.0")
	implementation("io.pivotal.services.dataTx:dataTx-geode-extensions-spring-security:2.5.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.github.nyla-solutions:nyla.solutions.core:1.4.3")
	implementation(project(":components:IoT-connected-vehicles-domains"))
	implementation(project(":components:iot-connected-vehicles-repository"))
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.geode:spring-geode-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}
dependencyManagement {
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
