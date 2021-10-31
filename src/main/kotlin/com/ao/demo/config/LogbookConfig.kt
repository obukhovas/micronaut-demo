package com.ao.demo.config

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import io.micronaut.http.netty.channel.ChannelPipelineCustomizer
import io.netty.channel.ChannelPipeline
import jakarta.inject.Singleton
import org.zalando.logbook.Conditions.exclude
import org.zalando.logbook.Conditions.requestTo
import org.zalando.logbook.DefaultHttpLogWriter
import org.zalando.logbook.DefaultSink
import org.zalando.logbook.Logbook
import org.zalando.logbook.json.JsonHttpLogFormatter
import org.zalando.logbook.netty.LogbookServerHandler

@Factory
class LogbookConfig {
    @Singleton
    fun logbook(): Logbook {
        return Logbook.builder()
            .condition(
                exclude(
                    requestTo("/beans"),
                    requestTo("/loggers"),
                    requestTo("/info"),
                    requestTo("/health"),
                    requestTo("/routes"),
                    requestTo("/metrics/**"),
                    requestTo("/prometheus"),
                    requestTo("/env/**")
                )
            )
            .sink(DefaultSink(JsonHttpLogFormatter(), DefaultHttpLogWriter()))
            .build()
    }
}

// Note: for keep-alive connections logs for only the first requests and response are printed
// https://github.com/zalando/logbook/issues/884
@Requires(beans = [Logbook::class])
@Singleton
class LogbookPipelineCustomizer(
    private val logbook: Logbook
) : BeanCreatedEventListener<ChannelPipelineCustomizer> {

    override fun onCreated(event: BeanCreatedEvent<ChannelPipelineCustomizer>): ChannelPipelineCustomizer {
        val customizer = event.bean
        if (customizer.isServerChannel) {
            customizer.doOnConnect { pipeline: ChannelPipeline ->
                pipeline.addAfter(
                    ChannelPipelineCustomizer.HANDLER_HTTP_SERVER_CODEC,
                    "logbook",
                    LogbookServerHandler(logbook)
                )
            }
        }
        return customizer
    }
}