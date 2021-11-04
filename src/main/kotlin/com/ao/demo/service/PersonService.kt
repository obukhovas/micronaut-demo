package com.ao.demo.service

import com.ao.demo.UserNotFoundException
import com.ao.demo.domain.Person
import com.ao.demo.repository.PersonRepository
import jakarta.inject.Singleton
import javax.transaction.Transactional
import javax.validation.ConstraintViolationException

@Singleton
open class PersonService(
    private val personRepository: PersonRepository,
    private val personConverter: PersonConverter
) {

    open fun getAll(): List<Person> {
        return personRepository.findAll().map {
            personConverter.fromEntity(it)
        }
    }

    open fun findById(id: Long): Person {
        return personRepository.findById(id)
            .orElseThrow { UserNotFoundException() }
            .let { personConverter.fromEntity(it) }
    }

    open fun findByName(name: String): List<Person> {
        return personRepository.findByFirstName(name).map {
            personConverter.fromEntity(it)
        }
    }

    @Transactional
    open fun create(person: Person): Person {
        val created = personRepository.save(
            personConverter.toEntity(person)
        )
        return personConverter.fromEntity(created)
    }

    @Transactional
    open fun update(id: Long, person: Person): Person {
        val entity = personRepository.findByIdForUpdate(id) ?: throw UserNotFoundException()
        with(entity) {
            firstName = person.firstName
            secondName = person.secondName
            age = person.age
        }
        return personRepository.update(entity).let {
            personConverter.fromEntity(it)
        }
    }

    @Transactional
    open fun delete(id: Long) {
        try {
            personRepository.deleteById(id)
        } catch (e: ConstraintViolationException) {
            throw UserNotFoundException()
        }
    }

}