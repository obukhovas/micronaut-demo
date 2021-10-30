package com.ao.demo.service

import com.ao.demo.domain.Person
import com.ao.demo.dto.PersonDto
import jakarta.inject.Singleton

@Singleton
class PersonConverter {

    fun fromDto(dto: PersonDto): Person {
        return with(dto) {
            Person(
                id = id,
                firstName = firstName,
                secondName = secondName,
                age = age
            )
        }
    }

    fun toDto(person: Person): PersonDto {
        return with(person) {
            PersonDto(
                id = id,
                firstName = firstName,
                secondName = secondName,
                age = age
            )
        }
    }

}