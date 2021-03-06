plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("org.graalvm.buildtools.native")
}

group = "com.johnturkson.template.server"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(project(":common"))
    implementation("com.johnturkson.cdk:cdk-generator:0.0.3")
    ksp("com.johnturkson.cdk:cdk-generator:0.0.3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("io.ktor:ktor-client-core-jvm:2.0.1")
    implementation("io.ktor:ktor-client-cio-jvm:2.0.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.0.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")
    implementation("com.johnturkson.security:security-tools:0.0.7")
    implementation("com.johnturkson.text:text-tools:0.0.4")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("org.springframework.security:spring-security-crypto:5.6.3")
    implementation("com.amazonaws:aws-lambda-java-core:1.2.1")
    implementation("com.amazonaws:aws-lambda-java-events:3.11.0")
    implementation("com.amazonaws:aws-lambda-java-runtime-interface-client:2.1.1")
    compileOnly("software.amazon.awscdk:aws-cdk-lib:2.24.1")
    compileOnly("software.amazon.awscdk:apigatewayv2-alpha:2.24.1-alpha.0")
    compileOnly("software.amazon.awscdk:apigatewayv2-integrations-alpha:2.24.1-alpha.0")
    implementation(platform("software.amazon.awssdk:bom:2.16.104"))
    implementation("software.amazon.awssdk:netty-nio-client")
    implementation("software.amazon.awssdk:dynamodb-enhanced") {
        exclude("software.amazon.awssdk", "netty-nio-client")
        exclude("software.amazon.awssdk", "apache-client")
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
    arg("hostedZone", "johnturkson.com")
    arg("routeSelectionExpression", "\$request.body.type")
    arg("HANDLER_LOCATION", "../server/build/lambda/image/${project.name}.zip")
}

graalvmNative {
    binaries {
        named("main") {
            mainClass.set("com.amazonaws.services.lambda.runtime.api.client.AWSLambda")
            verbose.set(true)
            buildArgs.add("--no-fallback")
            buildArgs.add("--enable-url-protocols=http")
            buildArgs.add("--initialize-at-build-time=org.slf4j")
        }
    }
}

tasks.register<Zip>("buildLambdaImage") {
    dependsOn("nativeCompile")
    dependsOn("buildLambdaBootstrap")
    archiveFileName.set("${project.name}.zip")
    destinationDirectory.set(file("$buildDir/lambda/image"))
    from("$buildDir/native/nativeCompile")
    from("$buildDir/lambda/bootstrap")
}

tasks.register("buildLambdaBootstrap") {
    mkdir("$buildDir/lambda/bootstrap")
    File("$buildDir/lambda/bootstrap", "bootstrap").bufferedWriter().use { writer ->
        writer.write(
            """
            #!/usr/bin/env bash
            
            ./${project.name} ${"$"}_HANDLER
            """.trimIndent()
        )
    }
}
