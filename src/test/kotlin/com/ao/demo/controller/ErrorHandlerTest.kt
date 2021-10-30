package com.ao.demo.controller

import com.ao.demo.BaseControllerTest
import com.ao.demo.minify
import com.nhaarman.mockitokotlin2.whenever
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

class ErrorHandlerTest : BaseControllerTest() {

    @Test
    fun `global error handler should return status 500 if unknown error`() {
        @Language("JSON")
        val expectedBody = """
            {
              "message": "Internal Server Error",
              "_links": {
                "self": {
                  "href": "/persons",
                  "templated": false
                }
              },
              "_embedded": {
                "errors": [
                  {
                    "message": "Unexpected error: Something went wrong"
                  }
                ]
              }
            }
        """.minify()

        whenever(personServiceMock.getAll()).thenThrow(RuntimeException("Something went wrong"))

        val request = HttpRequest.GET<Any>("/persons")
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo(expectedBody)
        }
    }

    @Test
    fun `global error handler should return status 400 if json invalid`() {
        val requestBody = """
            {
              believeMe: "I'm Json!"
            }
        """.trimIndent()

        @Language("JSON")
        val expectedBody = """
          {
            "message": "Invalid JSON: Unexpected character ('b' (code 98)): was expecting double-quote to start field name\n at [Source: UNKNOWN; line: 2, column: 4]",
            "_links": {
              "self": {
              "href": "/persons",
              "templated": false
            }
          }
        }
        """.minify()

        val request = HttpRequest.POST("/persons", requestBody)
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo(expectedBody)
        }
    }

}