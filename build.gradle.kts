plugins {
  application
  kotlin("jvm") version "1.9.20"
  kotlin("plugin.serialization") version "1.9.20"
}

group = "ca.alad"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val ktor_version: String by project

dependencies {
  testImplementation(kotlin("test"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0-RC")
  implementation("io.ktor:ktor-client-core:$ktor_version")
  implementation("io.ktor:ktor-client-cio:$ktor_version")
  implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
}

tasks.test {
  useJUnitPlatform()
}

application {
  mainClass.set("MainKt")
}
