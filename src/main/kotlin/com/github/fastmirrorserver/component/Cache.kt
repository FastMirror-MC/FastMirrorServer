package com.github.fastmirrorserver.component

interface Cache {
    fun <T> add(key: String, value: T): Boolean
    fun <T> add(key: String, value: T, ttl: Int): Boolean
    fun <T> add(key: String, value: T, expire: Long): Boolean
    fun <T> upd(key: String, value: T): Boolean
    fun <T> upd(key: String, value: T, ttl: Int): Boolean
    fun <T> upd(key: String, value: T, expire: Long): Boolean
    fun <T> get(key: String, type: Class<T>): T?
    fun get(key: String): String?
    fun has(key: String): Boolean
    fun del(key: String): Boolean
}