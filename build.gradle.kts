import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "com.dasoops"
version = "1.0"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    //common
    api("cn.hutool:hutool-all:5.8.18")
    api("com.google.guava:guava:31.1-jre")
    api(platform("com.dasoops:common-bom:4.1.5"))
    api("com.dasoops:common-core")
    api("com.dasoops:common-json-core")
    api("com.dasoops:common-json-jackson")
    api("org.apache.commons:commons-compress:1.9")

    //jna
    api("net.java.dev.jna:jna:5.2.0")
    api("net.java.dev.jna:jna-platform:5.2.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}