package com.johnturkson.template.infrastructure

import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.constructs.Construct

class TemplateStack(
    parent: Construct,
    name: String,
    props: StackProps? = null,
) : Stack(parent, name, props) {
    init {
        
    }
}
