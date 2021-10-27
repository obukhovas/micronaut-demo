package com.ao.demo.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@MicronautTest
class HelloControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testPing() {
        val request: HttpRequest<Any> = HttpRequest.GET("/ping")
        val response = client.toBlocking().exchange(request, String::class.java)
        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo("pong")
        }
    }

    @Test
    fun testHello() {
        val request: HttpRequest<Any> = HttpRequest.GET("/hello/World")
        val response = client.toBlocking().exchange(request, String::class.java)
        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo("Hello, World!")
        }
    }
}