plugins {
    java
    checkstyle
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
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

tasks {
    test {
        useJUnitPlatform()
    }
}

