package com.github.fastmirrorserver.components

import java.time.LocalDateTime

interface Cache {
    fun<T> add(key: String, value: T): Boolean
    fun<T> add(key: String, value: T, expire: Long): Boolean
    fun<T> add(key: String, value: T, expire: LocalDateTime): Boolean
    fun<T> upd(key: String, value: T)
    fun<T> upd(key: String, value: T, expire: Long)
    fun<T> upd(key: String, value: T, expire: LocalDateTime)
    fun<T> get(key: String, type: Class<T>): T?
    fun<T> pop(key: String, type: Class<T>): T?
    fun has(key: String): Boolean
    fun del(key: String): Boolean
}