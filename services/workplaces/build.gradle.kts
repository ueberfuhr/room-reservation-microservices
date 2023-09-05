import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.swagger.codegen.v3.config.CodegenConfigurator
import io.swagger.codegen.v3.DefaultGenerator
import io.swagger.v3.parser.OpenAPIV3Parser

val swaggerSourceFile = "${projectDir}/docs/openapi.yml"
val swaggerTargetFolder = "${projectDir}/src/main/resources/static/api-docs"

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.swagger.codegen.v3:swagger-codegen-generators:1.0.42")
        classpath("io.swagger.codegen.v3:swagger-codegen:3.0.22")
    }
}

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
}

group = "de.samples.room-reservation"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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

tasks.register("copyAndMergeOpenApi") {
    doLast {
        val config = CodegenConfigurator()
        config.setInputSpecURL(swaggerSourceFile)
        config.setOutputDir(swaggerTargetFolder)
        config.setLang("openapi-yaml")
        val opts = config.toClientOptInput()
        opts.openAPI = OpenAPIV3Parser().read(swaggerSourceFile)
        DefaultGenerator().opts(opts).generate()
    }
}

tasks {
    "build" {
        dependsOn("copyAndMergeOpenApi")
    }
}
