package com.ao.demo.controller

import com.ao.demo.BaseControllerTest
import com.ao.demo.buildLongString
import com.ao.demo.minify
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class PersonViolationsControllerTest : BaseControllerTest() {

    @TestFactory
    fun `person dto violations`(): List<DynamicTest> {
        @Language("JSON")
        val requestBody = """
            {
              "firstName": ${buildLongString(31)},
              "secondName": ${buildLongString(51)},
              "age": -1
            }
        """.minify()

        return listOf(
            HttpRequest.POST("/persons", requestBody),
            HttpRequest.PUT("/persons/99", requestBody)
        ).map { httpRequest ->
            DynamicTest.dynamicTest("${httpRequest.method} ${httpRequest.uri.path} violations") {
                @Language("JSON")
                val expectedBody = """
                    {
                      "message": "Bad Request",
                      "_links": {
                        "self": {
                          "href": "${httpRequest.uri.path}",
                          "templated": false
                        }
                      },
                      "_embedded": {
                        "errors": [
                          {
                            "message": "person.age: must be greater than or equal to 0"
                          },
                          {
                            "message": "person.firstName: size must be between 0 and 30"
                          },
                          {
                            "message": "person.secondName: size must be between 0 and 50"
                          }
                        ]
                      }
                    }
                """.minify()

                val response = perform(httpRequest)

                with(response) {
                    assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST)
                    assertThat(body.isPresent).isTrue
                    assertThat(body.get()).isEqualTo(expectedBody)
                }
            }
        }
    }

}