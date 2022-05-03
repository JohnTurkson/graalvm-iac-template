plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

group = "com.johnturkson.template.common"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation("com.johnturkson.cdk:cdk-generator:0.0.2")
    ksp("com.johnturkson.cdk:cdk-generator:0.0.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    compileOnly("software.amazon.awscdk:aws-cdk-lib:2.22.0")
    implementation(platform("software.amazon.awssdk:bom:2.16.104"))
    implementation("software.amazon.awssdk:dynamodb-enhanced") {
        exclude("software.amazon.awssdk", "apache-client")
        exclude("software.amazon.awssdk", "netty-nio-client")
    }
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir("build/generated/ksp/main/kotlin")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
        vendor.set(JvmVendorSpec.GRAAL_VM)
    }
}

ksp {
    arg("location", "$group.generated")
}
