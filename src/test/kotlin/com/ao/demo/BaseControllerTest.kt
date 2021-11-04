package com.ao.demo

import com.ao.demo.service.PersonService
import com.nhaarman.mockitokotlin2.mock
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions

@MicronautTest
abstract class BaseControllerTest {
    @Inject
    @field:Client("/")
    protected lateinit var client: HttpClient

    @Inject
    protected lateinit var personServiceMock: PersonService

    @MockBean(PersonService::class)
    fun personServiceMock() = mock<PersonService>()

    protected fun perform(request: HttpRequest<*>): HttpResponse<String> {
        return try {
            client.toBlocking().exchange(request, String::class.java)
        } catch (e: HttpClientResponseException) {
            @Suppress("UNCHECKED_CAST")
            e.response as HttpResponse<String>
        }
    }

    protected fun assertUnauthorizedIfNoCredentialsProvided(request: MutableHttpRequest<out Any>) {
        val response = perform(request)
        with(response) {
            Assertions.assertThat(status).isEqualTo(HttpStatus.UNAUTHORIZED)
            Assertions.assertThat(body.isPresent).isFalse
        }
    }

    protected fun assertUnauthorizedIfWrongPassword(request: MutableHttpRequest<out Any>) {
        val response = perform(request.basicAuth("reader", "wrong_password"))
        with(response) {
            Assertions.assertThat(status).isEqualTo(HttpStatus.UNAUTHORIZED)
            Assertions.assertThat(body.isPresent).isFalse
        }
    }

    protected fun assertForbiddenIfRoleCorrupted(request: MutableHttpRequest<out Any>) {
        val response = perform(request.basicAuth("anonymous", "anonymous"))
        with(response) {
            Assertions.assertThat(status).isEqualTo(HttpStatus.UNAUTHORIZED)
            Assertions.assertThat(body.isPresent).isFalse
        }
    }
}