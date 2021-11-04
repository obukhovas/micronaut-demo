package com.ao.demo.controller

import com.ao.demo.config.CredentialProperties
import io.micronaut.core.async.publisher.AsyncSingleResultPublisher
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH
import io.micronaut.security.authentication.AuthenticationFailureReason.USER_NOT_FOUND
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher

@Suppress("unused")
@Singleton
class BasicAuthenticationProvider(
    private val credentialProperties: CredentialProperties
) : AuthenticationProvider {
    override fun authenticate(
        httpRequest: HttpRequest<*>,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        val username = authenticationRequest.identity.toString()
        val password = authenticationRequest.secret.toString()

        val user = credentialProperties.users.find { it.username == username }
        val authenticationResponse = if (user == null) {
            AuthenticationFailed(USER_NOT_FOUND)
        } else if (user.password != password) {
            AuthenticationFailed(CREDENTIALS_DO_NOT_MATCH)
        } else {
            AuthenticationResponse.success(username, user.roles.toList())
        }

        return AsyncSingleResultPublisher { authenticationResponse }
    }

}