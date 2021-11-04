package com.ao.demo.repository

import com.ao.demo.entity.PersonEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository


@Repository
@JdbcRepository(dialect = Dialect.H2)
interface PersonRepository : CrudRepository<PersonEntity, Long> {

    fun findByFirstName(firstName: String): List<PersonEntity>

    fun findByIdForUpdate(id: Long): PersonEntity?

}