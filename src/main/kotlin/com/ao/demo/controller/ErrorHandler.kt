package com.ao.demo.controller

import com.ao.demo.UserNotFoundException
import com.fasterxml.jackson.core.JsonParseException
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Produces
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.hateoas.Link
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import jakarta.inject.Singleton

@Controller
class ErrorHandler {

    @Error(global = true)
    fun handle(request: HttpRequest<*>, ex: JsonParseException): HttpResponse<JsonError> {
        val error = JsonError("Invalid JSON: ${ex.message}")
            .link(Link.SELF, Link.of(request.uri))

        return HttpResponse.badRequest<JsonError>()
            .body(error)
    }

    @Error(global = true)
    fun handle(request: HttpRequest<*>, e: UserNotFoundException): HttpResponse<Any> {
        return HttpResponse.notFound()
    }

}

// Shouldn't be placed in ErrorHandler, because otherwise it will override Micronaut built-in handlers,
// since @Error annotation capturing an exception has precedence over an implementation of ExceptionHandler
// capturing the same exception, for instance UnsatisfiedArgumentHandler, UnsatisfiedRouteHandler, etc.
@Suppress("unused")
@Produces
@Singleton
@Requirements(Requires(classes = [ExceptionHandler::class]))
class UnexpectedErrorHandler(private val errorResponseProcessor: ErrorResponseProcessor<Any>) :
    ExceptionHandler<Throwable, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>, exception: Throwable): HttpResponse<*> {
        return errorResponseProcessor.processResponse(
            ErrorContext.builder(request)
                .cause(exception)
                .errorMessage("Unexpected error: ${exception.message}")
                .build(),
            HttpResponse.serverError<Any>("Unexpected error: ${exception.message}")
        )
    }
}