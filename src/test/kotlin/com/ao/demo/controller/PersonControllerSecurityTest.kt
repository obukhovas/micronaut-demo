package com.ao.demo.controller

import com.ao.demo.BaseControllerTest
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import io.micronaut.http.HttpRequest
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class PersonControllerSecurityTest : BaseControllerTest() {

    @TestFactory
    fun securityTest(): List<DynamicTest> {
        return listOf(
            HttpRequest.GET("/persons"),
            HttpRequest.GET("/persons?name=person"),
            HttpRequest.GET("/persons/99"),
            HttpRequest.POST("/persons", "{}"),
            HttpRequest.PUT("/persons/99", "{}"),
            HttpRequest.PUT("/persons/99", "{}"),
            HttpRequest.DELETE<Any>("/persons/99")
        ).map { request ->
            DynamicTest.dynamicTest("${request.method} ${request.uri.path} should fail if request has any credential problems") {
                assertUnauthorizedIfNoCredentialsProvided(request)
                assertUnauthorizedIfWrongPassword(request)
                assertForbiddenIfRoleCorrupted(request)
                verifyZeroInteractions(personServiceMock)
            }
        }
    }

}