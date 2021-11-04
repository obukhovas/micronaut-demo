package com.ao.demo.service

import com.ao.demo.domain.Person
import com.ao.demo.dto.PersonDto
import com.ao.demo.entity.PersonEntity
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

    fun fromEntity(entity: PersonEntity): Person {
        return with(entity) {
            Person(
                id = id,
                firstName = firstName,
                secondName = secondName,
                age = age
            )
        }
    }

    fun toEntity(person: Person): PersonEntity {
        return with(person) {
            PersonEntity(
                id = id,
                firstName = firstName,
                secondName = secondName,
                age = age
            )
        }
    }

}