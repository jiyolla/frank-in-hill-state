plugins {
    java
    id("io.freefair.lombok") version "8.6"
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "jiyolla"

repositories {
    mavenCentral()
}

val binanceConnectorVersion = "3.2.0"
val log4jVersion = "2.23.1"
val junitBomVersion = "5.10.2"
val ta4jVersion = "0.16"
val jacksonVersion = "2.17.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("org.ta4j:ta4j-core:$ta4jVersion")
    implementation("io.github.binance:binance-connector-java:$binanceConnectorVersion") {
        // 동일한 org.json.JSONObject를 spring boot test starter와 다른 artifact에서 로딩하고 있음
        // artifact가 같았으면 gradle이 알아서 artifact에 대해서 conflict resolution(높은 버전 사용)을 해줬을 것이지만
        // 다른 artifact에서 같은 class를 도입하는 거라 gradle레벨에서 해소 안 됨
        // 어떻게 보면 diamond dependency 문제. 서로 다른 중간 dependency에서 같은 하위 dependency를 인입하고 있고 버전이 다름
        exclude(group = "org.json", module = "json")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:$junitBomVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
