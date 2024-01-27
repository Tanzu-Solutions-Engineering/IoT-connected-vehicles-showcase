plugins {
	java
	id("org.springframework.boot") version "3.1.8"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.vmware"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	maven {
		credentials {
			username =  System.getenv("HARBOR_USER")
			password =  System.getenv("HARBOR_PASSWORD")
		}
		url = uri("https://commercial-repo.pivotal.io/data3/gemfire-release-repo/gemfire")
	}
}

extra["springCloudVersion"] = "2022.0.4"
extra["springDataForGemFire.version"] = "1.0.0"
extra["vmwareGemFireVersion"] = "10.0.0"

dependencies {

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	//------------
	implementation("com.vmware.gemfire:spring-data-3.1-gemfire-10.0:${property("springDataForGemFire.version")}")
	implementation("com.vmware.gemfire:gemfire-core:${property("vmwareGemFireVersion")}")
	//---------------------
	implementation("com.github.nyla-solutions:nyla.solutions.core:2.1.0")

	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.cloud:spring-cloud-stream")
	implementation(project(":components:vehicle-domain"))
	implementation("org.springframework.cloud:spring-cloud-stream-binder-rabbit")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}

