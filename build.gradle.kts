plugins {
    java
    checkstyle
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.netty:netty-handler:4.1.76.Final")

    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // https://mvnrepository.com/artifact/org.joml/joml-2d
    implementation("org.joml:joml-2d:1.6.0")

}

java {
    toolchain {
        // 设置构建使用指定 java 版本
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

checkstyle {
    maxWarnings = 0
    toolVersion = "10.0"
}

tasks{

    test {
        useJUnitPlatform()
    }

    compileJava{
        options.encoding = "UTF-8"
    }

}