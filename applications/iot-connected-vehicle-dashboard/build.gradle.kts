import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.1"
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
extra["geodeVersion"] = "1.13.1"


dependencies {

	implementation("io.pivotal.services.dataTx:dataTx-geode-extensions-core:2.4.0")
	implementation("io.pivotal.services.dataTx:dataTx-geode-extensions-spring-security:2.5.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.github.nyla-solutions:nyla.solutions.core:1.4.4")
	implementation(project(":components:IoT-connected-vehicles-domains"))
	implementation(project(":components:iot-connected-vehicles-repository"))
	implementation(project(":components:geode-listener"))
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.geode:spring-geode-starter") {
		exclude(group = "org.apache.geode")
	}

	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.webjars:webjars-locator-core")
	implementation("org.webjars:sockjs-client:1.0.2")
	implementation("org.webjars:stomp-websocket:2.3.3")
	implementation("org.webjars:bootstrap:3.3.7")
//	implementation("org.webjars:jquery:3.1.1-1")

	implementation("org.apache.geode:geode-core:${property("geodeVersion")}")
	implementation("org.apache.geode:geode-cq:${property("geodeVersion")}")
	implementation("org.apache.geode:geode-lucene:${property("geodeVersion")}")
	implementation("org.apache.geode:geode-wan:${property("geodeVersion")}")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

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
