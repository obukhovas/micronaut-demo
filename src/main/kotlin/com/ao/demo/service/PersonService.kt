package com.ao.demo.service

import com.ao.demo.UserNotFoundException
import com.ao.demo.domain.Person
import jakarta.inject.Singleton
import java.util.Collections
import java.util.concurrent.atomic.AtomicLong

@Singleton
open class PersonService {

    private companion object {
        val counter = AtomicLong()
        val persons: MutableList<Person> = Collections.synchronizedList(mutableListOf<Person>())
    }

    open fun getAll(): List<Person> {
        return persons.toList()
    }

    open fun findById(id: Long): Person {
        return persons.firstOrNull { id == it.id } ?: throw UserNotFoundException()
    }

    open fun findByName(name: String): List<Person> {
        return persons.filter { name.equals(it.firstName, ignoreCase = true) }
    }

    open fun create(person: Person): Person {
        return person.also {
            it.id = counter.incrementAndGet()
            persons.add(it)
        }
    }

    open fun update(person: Person): Person {
        return findById(person.id!!).also {
            it.firstName = person.firstName
            it.secondName = person.secondName
            it.age = person.age
        }
    }

    open fun delete(id: Long): Person {
        return findById(id).also {
            persons.remove(it)
        }
    }

}