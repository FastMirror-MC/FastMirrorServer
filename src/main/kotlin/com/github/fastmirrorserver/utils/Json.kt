package com.github.fastmirrorserver.utils

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object Json {
    private val mapper = ObjectMapper().also {
        it.registerKotlinModule()
        it.registerModule(JavaTimeModule())
//        it.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        it.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        it.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    }
    
    fun <T> serialization(value: T): String = mapper.writeValueAsString(value)
    fun <T> deserialization(src: String, type: Class<T>): T = mapper.readValue(src, type)
}

inline fun <reified T> T.serialize() = Json.serialization(this)
inline fun <reified T> String.deserialize(): T? = Json.deserialization(this, T::class.java)