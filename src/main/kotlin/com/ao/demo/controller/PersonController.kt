package com.ao.demo.controller

import com.ao.demo.dto.PersonDto
import com.ao.demo.service.PersonConverter
import com.ao.demo.service.PersonService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import javax.validation.Valid

@Controller
@Secured(SecurityRule.IS_AUTHENTICATED)
open class PersonController(
    private val personService: PersonService,
    private val personConverter: PersonConverter
) {

    // Micronaut does not consider query parameters when matching a route,
    // so separating the routes by query parameters will never work
    @Get("/persons", processes = [MediaType.APPLICATION_JSON])
    @Secured("ROLE_READER")
    fun findAllOrByName(@QueryValue name: String?): HttpResponse<List<PersonDto>> {
        val persons = if (name != null) personService.findByName(name) else personService.getAll()

        if (persons.isEmpty()) {
            return HttpResponse.noContent()
        }

        return HttpResponse.ok(
            persons.map {
                personConverter.toDto(it)
            }
        )
    }

    @Get("/persons/{id}", processes = [MediaType.APPLICATION_JSON])
    @Secured("ROLE_READER")
    fun getById(@PathVariable id: Long): HttpResponse<PersonDto> {
        val person = personService.findById(id)
        return HttpResponse.ok(personConverter.toDto(person))
    }

    @Post("/persons", consumes = [MediaType.APPLICATION_JSON], processes = [MediaType.APPLICATION_JSON])
    @Secured("ROLE_EDITOR")
    open fun create(@Valid person: PersonDto): HttpResponse<PersonDto> {
        val created = personService.create(
            personConverter.fromDto(person)
        )
        return HttpResponse.created(personConverter.toDto(created))
    }

    @Put("/persons/{id}", consumes = [MediaType.APPLICATION_JSON], processes = [MediaType.APPLICATION_JSON])
    @Secured("ROLE_EDITOR")
    open fun update(@PathVariable id: Long, @Valid person: PersonDto): HttpResponse<PersonDto> {
        val updated = personService.update(
            id = id,
            person = personConverter.fromDto(person)
        )
        return HttpResponse.ok(personConverter.toDto(updated))
    }

    @Delete("persons/{id}", processes = [MediaType.APPLICATION_JSON])
    @Secured("ROLE_EDITOR")
    open fun delete(@PathVariable id: Long): HttpResponse<Any> {
        personService.delete(id)
        return HttpResponse.ok()
    }

}