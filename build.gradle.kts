plugins {
    kotlin("jvm") version "1.7.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}