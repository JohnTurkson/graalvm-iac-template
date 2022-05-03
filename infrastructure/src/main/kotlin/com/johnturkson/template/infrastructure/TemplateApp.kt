package com.johnturkson.template.infrastructure

import software.amazon.awscdk.App
import software.amazon.awscdk.Environment
import software.amazon.awscdk.StackProps

fun main() {
    val app = App()
    val environment = Environment.builder()
        .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
        .region(System.getenv("CDK_DEFAULT_REGION"))
        .build()
    TemplateStack(app, "TemplateStack", StackProps.builder().env(environment).build())
    app.synth()
}
