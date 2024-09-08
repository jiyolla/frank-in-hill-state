plugins {
    alias(libs.plugins.my.convention)
    alias(libs.plugins.lombok)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.jib)
}

dependencies {
    implementation(libs.ta4j)
    implementation(libs.binance.connector)
    implementation(libs.spring.boot.starter)
    implementation(libs.jackson.databind)
}

jib {
    from {
        image = "amazoncorretto:21-alpine3.20"
    }
    to {
        image = "jiyolla/frank-in-hill-state"
        auth {
            username = System.getenv("DOCKER_HUB_USERNAME")
            password = System.getenv("DOCKER_HUB_PASSWORD")
        }
    }
}
