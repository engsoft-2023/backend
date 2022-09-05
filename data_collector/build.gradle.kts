plugins {
	id("org.springframework.boot") version "2.7.0" apply false
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

dependencies {
	implementation(project(":domain"))
	implementation(project(":persistence"))
	implementation("org.yaml:snakeyaml:1.30")
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
}
