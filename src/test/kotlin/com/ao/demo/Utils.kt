package com.ao.demo

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser


fun buildLongString(times: Int, string: String = "1"): String {
    return buildString {
        repeat(times) { append(string) }
    }
}

fun String.minify(): String {
    val gson = GsonBuilder().disableHtmlEscaping().create()
    val jsonElement = JsonParser.parseString(this)
    return gson.toJson(jsonElement)
}