package com.ao.demo.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
class HelloController {

    @Get("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    fun ping() = "pong"

    @Get("/hello/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(@PathVariable name: String): String {
        return "Hello, $name!"
    }

}