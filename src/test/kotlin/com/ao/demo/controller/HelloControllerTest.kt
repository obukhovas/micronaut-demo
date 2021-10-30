package com.ao.demo.controller

import com.ao.demo.BaseControllerTest
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HelloControllerTest : BaseControllerTest() {

    @Test
    fun testPing() {
        val request: HttpRequest<Any> = HttpRequest.GET("/ping")
        val response = perform(request)
        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo("pong")
        }
    }

    @Test
    fun testHello() {
        val request: HttpRequest<Any> = HttpRequest.GET("/hello/World")
        val response = perform(request)
        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo("Hello, World!")
        }
    }
}