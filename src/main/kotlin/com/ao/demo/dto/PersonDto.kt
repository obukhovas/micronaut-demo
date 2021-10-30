package com.ao.demo.dto

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class PersonDto(
    val id: Long? = null,
    @field:[NotBlank Size(max = 30)]
    val firstName: String,
    @field:[NotBlank Size(max = 50)]
    val secondName: String,
    @field:Min(0)
    val age: Int
)