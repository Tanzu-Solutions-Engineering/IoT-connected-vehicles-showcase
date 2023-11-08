import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//	kotlin("jvm") version "1.4.30"
//	kotlin("plugin.spring") version "1.4.30"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "com.vmware.tanzu.data"
version = "0.0.1-SNAPSHOT"
java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
	testImplementation("org.mockito:mockito-core:5.6.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.6.0")
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
