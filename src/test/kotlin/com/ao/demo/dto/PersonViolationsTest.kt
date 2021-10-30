package com.ao.demo.dto

import com.ao.demo.BaseControllerTest
import com.ao.demo.buildLongString
import io.micronaut.validation.validator.Validator
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PersonViolationsTest : BaseControllerTest() {
    @Inject
    private lateinit var validator: Validator

    @Test
    fun `person violations test`() {
        val invalid = PersonDto(
            firstName = buildLongString(31),
            secondName = "",
            age = -1
        )

        val violations = validator.validate(invalid)

        assertThat(violations.size).isEqualTo(3)
        assertThat(violations.map { it.message }).containsAll(
            setOf(
                "must not be blank",
                "size must be between 0 and 30",
                "must be greater than or equal to 0"
            )
        )
    }

}