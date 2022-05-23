plugins {
    java
    checkstyle
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.netty:netty-handler:4.1.76.Final")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.springframework:spring-webmvc:5.3.19")

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

tasks{

    test {
        useJUnitPlatform()
    }

    compileJava{
        options.encoding = "UTF-8"
    }

}