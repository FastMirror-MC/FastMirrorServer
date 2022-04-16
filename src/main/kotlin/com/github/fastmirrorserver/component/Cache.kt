package com.github.fastmirrorserver.component

interface Cache {

    fun <T> add(key: String, value: T): Boolean
    fun <T> add(key: String, value: T, ttl: Int): Boolean
    fun <T> add(key: String, value: T, expire: Long): Boolean
    fun <T> get(key: String, type: Class<T>): T?
    fun get(key: String): String?
    fun del(key: String): Boolean
}