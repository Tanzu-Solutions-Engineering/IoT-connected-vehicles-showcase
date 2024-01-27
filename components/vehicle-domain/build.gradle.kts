plugins {
	// Apply the application plugin to add support for building a CLI application in Java.
	java
}

group = "com.vmware.tanzu.data"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	mavenLocal()
}

dependencies {
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")

	testCompileOnly("org.projectlombok:lombok:1.18.30")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
	testImplementation("org.mockito:mockito-core:5.6.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.6.0")
}

//java {
//	sourceCompatibility = JavaVersion.VERSION_17
//}

//java {
//	toolchain {
//		languageVersion.set(JavaLanguageVersion.of(17))
//	}
//}

tasks.withType<Test> {
	useJUnitPlatform()
}
