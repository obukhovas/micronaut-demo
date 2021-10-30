package com.ao.demo

import com.ao.demo.service.PersonService
import com.nhaarman.mockitokotlin2.mock
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest
abstract class BaseControllerTest {
    @Inject
    @field:Client("/")
    protected lateinit var client: HttpClient

    @Inject
    protected lateinit var personServiceMock: PersonService

    protected fun perform(request: HttpRequest<*>): HttpResponse<String> {
        return try {
            client.toBlocking().exchange(request, String::class.java)
        } catch (e: HttpClientResponseException) {
            @Suppress("UNCHECKED_CAST")
            e.response as HttpResponse<String>
        }
    }

    @MockBean(PersonService::class)
    fun personServiceMock() = mock<PersonService>()
}