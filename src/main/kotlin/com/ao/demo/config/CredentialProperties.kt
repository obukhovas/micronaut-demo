package com.ao.demo.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Introspected
@ConfigurationProperties("credentials")
class CredentialProperties {
    @NotEmpty
    var users: Collection<User> = emptyList()
}

@Introspected
class User {
    @NotBlank
    var username: String = ""
    @NotBlank
    var password: String = ""
    @NotEmpty
    var roles: Collection<String> = emptyList()
}
