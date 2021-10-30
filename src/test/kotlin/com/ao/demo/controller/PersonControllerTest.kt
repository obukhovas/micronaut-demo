package com.ao.demo.controller

import com.ao.demo.BaseControllerTest
import com.ao.demo.UserNotFoundException
import com.ao.demo.minify
import com.ao.demo.domain.Person
import com.nhaarman.mockitokotlin2.whenever
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers

class PersonControllerTest : BaseControllerTest() {

    @Test
    fun `find all should return status 200 and persons list`() {
        @Language("JSON")
        val expectedBody = """
            [
                {
                  "id": 1,
                  "firstName": "Great",
                  "secondName": "Person",
                  "age": 20
                }
            ]
        """.minify()
        whenever(personServiceMock.getAll())
            .thenReturn(listOf(Person(1L, "Great", "Person", 20)))

        val request = HttpRequest.GET<Any>("/persons")
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo(expectedBody)
        }
    }

    @Test
    fun `find all should return status 204 if there is no persons`() {
        whenever(personServiceMock.getAll()).thenReturn(emptyList())

        val request = HttpRequest.GET<Any>("/persons")
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(body.isPresent).isFalse
        }
    }

    @Test
    fun `find by name should return status 200 and found person`() {
        @Language("JSON")
        val expectedBody = """
            [
                {
                  "id": 1,
                  "firstName": "Great",
                  "secondName": "Person",
                  "age": 20
                }
            ]
        """.minify()
        whenever(personServiceMock.findByName("person"))
            .thenReturn(listOf(Person(1L, "Great", "Person", 20)))

        val request = HttpRequest.GET<Any>("/persons?name=person")
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo(expectedBody)
        }
    }

    @Test
    fun `find by name should return status 204 if person does not found`() {
        whenever(personServiceMock.findByName("person")).thenReturn(emptyList())

        val request = HttpRequest.GET<Any>("/persons?name=person")
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(body.isPresent).isFalse
        }
    }

    @Test
    fun `getById should return status 200 and found person`() {
        @Language("JSON")
        val expectedBody = """
            {
              "id": 1,
              "firstName": "Great",
              "secondName": "Person",
              "age": 20
            }
        """.minify()
        whenever(personServiceMock.findById(99L)).thenReturn(Person(1L, "Great", "Person", 20))

        val request = HttpRequest.GET<Any>("/persons/99")
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo(expectedBody)
        }
    }

    @Test
    fun `getById should return status 404 if person does not found`() {
        whenever(personServiceMock.findById(99L)).thenThrow(UserNotFoundException())

        val request = HttpRequest.GET<Any>("/persons/99")
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.NOT_FOUND)
            assertThat(body.isPresent).isFalse
        }
    }

    @Test
    fun `createPerson should return status 201 and created person`() {
        @Language("JSON")
        val requestBody = """
            {
              "firstName": "Great",
              "secondName": "Person",
              "age": 20
            }
        """.minify()
        @Language("JSON")

        val expectedBody = """
            {
              "id": 1,
              "firstName": "Great",
              "secondName": "Person",
              "age": 20
            }
        """.minify()
        whenever(personServiceMock.create(Person(null, "Great", "Person", 20)))
            .thenReturn(Person(1L, "Great", "Person", 20))

        val request = HttpRequest.POST("/persons", requestBody)
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.CREATED)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo(expectedBody)
        }
    }

    @Test
    fun `updatePerson should return status 200 and updated person`() {
        @Language("JSON")
        val requestBody = """
            {
              "firstName": "Greatest",
              "secondName": "Man",
              "age": 18
            }
        """.minify()

        @Language("JSON")
        val expectedBody = """
            {
              "id": 99,
              "firstName": "Greatest",
              "secondName": "Man",
              "age": 18
            }
        """.minify()
        whenever(personServiceMock.update(Person(99, "Greatest", "Man", 18)))
            .thenAnswer(AdditionalAnswers.returnsFirstArg<Person>())


        val request = HttpRequest.PUT("/persons/99", requestBody)
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo(expectedBody)
        }
    }

    @Test
    fun `updatePerson should return status 404 if person does not found`() {
        whenever(personServiceMock.update(Person(99, "Greatest", "Man", 18))).thenThrow(UserNotFoundException())

        @Language("JSON")
        val requestBody = """
            {
              "firstName": "Greatest",
              "secondName": "Man",
              "age": 18
            }
        """.minify()

        val request = HttpRequest.PUT("/persons/99", requestBody)
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.NOT_FOUND)
            assertThat(body.isPresent).isFalse
        }
    }

    @Test
    fun `deletePerson should return status 200 and deleted person`() {
        @Language("JSON")
        val expectedBody = """
            {
              "id": 99,
              "firstName": "Greatest",
              "secondName": "Man",
              "age": 18
            }
        """.minify()
        whenever(personServiceMock.delete(99)).thenReturn(Person(99, "Greatest", "Man", 18))

        val request = HttpRequest.DELETE<Any>("/persons/99")
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.OK)
            assertThat(body.isPresent).isTrue
            assertThat(body.get()).isEqualTo(expectedBody)
        }
    }

    @Test
    fun `deletePerson should return status 404 if person does not found`() {
        whenever(personServiceMock.delete(99)).thenThrow(UserNotFoundException())

        val request = HttpRequest.DELETE<Any>("/persons/99")
        val response = perform(request)

        with(response) {
            assertThat(status).isEqualTo(HttpStatus.NOT_FOUND)
            assertThat(body.isPresent).isFalse
        }
    }

}