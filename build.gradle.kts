plugins {
    java
    id("net.kyori.blossom") version "1.3.0"
}

group = "com.hpfxd"
version = "1.0.0"

repositories {
    mavenCentral()
    maven(url = "https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)
}

blossom {
    val constants = "src/main/java/com/hpfxd/velocityplayerlimit/Constants.java"
    replaceToken("@version@", project.version, constants)
}
