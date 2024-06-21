import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	id("java-library")
	id("java-test-fixtures")
	id ("maven-publish")
}

group = "com.croco.interview"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	mavenLocal()
}

dependencies {

	api("org.springframework.boot:spring-boot-starter-data-jpa")
	api("org.springframework.boot:spring-boot-starter-security")
	api("org.springframework.boot:spring-boot-starter-web")
	api("org.springframework.kafka:spring-kafka")
	api("com.auth0:java-jwt:4.4.0")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testFixturesApi("org.springframework.boot:spring-boot-starter-test")
	testFixturesApi("org.springframework.boot:spring-boot-testcontainers")
	testFixturesApi("org.springframework.kafka:spring-kafka-test")
	testFixturesApi("org.springframework.security:spring-security-test")
	testFixturesApi("org.testcontainers:junit-jupiter")
	testFixturesApi("org.testcontainers:kafka")
	testFixturesApi("org.testcontainers:postgresql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = project.group.toString()
			artifactId = project.name
			version = project.version.toString()

			from(components["java"])
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.getByName<BootJar>("bootJar") {
	enabled = false
}

tasks.getByName<Jar>("jar") {
	enabled = true
}
