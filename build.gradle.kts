plugins {
  id("org.springframework.boot") version "3.3.2"
  id("io.spring.dependency-management") version "1.1.5"
  java
}

group = "com.fiap"
version = "1.0.0"
java {
  sourceCompatibility = JavaVersion.VERSION_17
}

repositories { mavenCentral() }

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  runtimeOnly("org.postgresql:postgresql")
  implementation("org.flywaydb:flyway-core:10.10.0")
  implementation("org.flywaydb:flyway-database-postgresql:10.10.0")
  implementation("org.webjars:bootstrap:5.3.3")
  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
  useJUnitPlatform()
}