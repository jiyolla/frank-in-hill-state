plugins {
    alias(libs.plugins.my.convention)
    alias(libs.plugins.lombok)
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(libs.ta4j)
    implementation(libs.binance.connector)
    implementation(libs.spring.boot.starter)
    implementation(libs.jackson.databind)
}
