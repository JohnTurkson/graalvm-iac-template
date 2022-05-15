plugins {
    kotlin("jvm")
    application
}

group = "com.johnturkson.template.infrastructure"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(project(":common"))
    implementation(project(":server"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation("software.amazon.awscdk:aws-cdk-lib:2.24.1")
    implementation("software.amazon.awscdk:apigatewayv2-alpha:2.24.1-alpha.0")
    implementation("software.amazon.awscdk:apigatewayv2-integrations-alpha:2.24.1-alpha.0")
}

application {
    mainClass.set("com.johnturkson.template.infrastructure.TemplateAppKt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
        vendor.set(JvmVendorSpec.GRAAL_VM)
    }
}
