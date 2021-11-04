package com.ao.demo.config

import io.micronaut.context.annotation.Factory
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(
        title = "Persons API",
        version = "0.0",
        description = "Micronaut demo API"
    )
)
@Factory
class OpenApiConfig