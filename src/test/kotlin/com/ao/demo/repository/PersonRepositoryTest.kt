package com.ao.demo.repository

import com.ao.demo.entity.PersonEntity
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.transaction.SynchronousTransactionManager
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.TimeoutException

@MicronautTest(transactional = false, startApplication = false)
class PersonRepositoryTest {

    @Inject
    private lateinit var transactionManager: SynchronousTransactionManager<*>

    @Inject
    private lateinit var repository: PersonRepository

    @BeforeEach
    fun cleanUp() {
        repository.deleteAll()
    }

    @Test
    fun findByFirstName() {
        repository.saveAll(
            listOf(
                PersonEntity(null, "1", "1", 1),
                PersonEntity(null, "2", "2", 2),
                PersonEntity(null, "3", "3", 3)
            )
        )

        val actual = repository.findByFirstName("2")

        assertThat(actual.size).isEqualTo(1)
        assertThat(actual.single())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(PersonEntity(null, "2", "2", 2))
    }

    @Test
    fun `findByIdForUpdate should lock entity`() {
        val id = repository.save(PersonEntity(null, "1", "1", 1)).id!!
        repository.findByIdForUpdate(id)

        val firstFindForUpdateLatch = CountDownLatch(1)
        val secondFindForUpdateLatch = CountDownLatch(1)
        val pool = Executors.newFixedThreadPool(2)

        val firstFindForUpdate = pool.submit {
            transactionManager.executeRead {
                repository.findByIdForUpdate(id)
                firstFindForUpdateLatch.countDown()
                secondFindForUpdateLatch.await()
            }
        }

        val secondFindForUpdate = pool.submit {
            transactionManager.executeRead {
                firstFindForUpdateLatch.await()
                repository.findByIdForUpdate(id)
            }
        }

        assertThrows<TimeoutException> { secondFindForUpdate.get(3, SECONDS) }
        secondFindForUpdateLatch.countDown()
        firstFindForUpdate.get()
    }

}