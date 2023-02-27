package com.github.fastmirrorserver.components

import com.github.fastmirrorserver.utils.Json
import com.github.fastmirrorserver.utils.timestamp
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime
import java.util.TreeMap

@Component
class MemoryCache : Cache {
    private class Node<T>(value: T, val expire: Long = Int.MAX_VALUE + timestamp()) {
        private val _value = Json.serialization(value)
        fun<T> convert(type: Class<T>): T = Json.deserialization(_value, type)
    }
    private val cache = TreeMap<String, Node<*>>();
    
    override fun <T> add(key: String, value: T): Boolean {
        if(has(key)) return false
        cache[key] = Node(value)
        return true
    }

    override fun <T> add(key: String, value: T, expire: Long): Boolean {
        if(has(key)) return false
        cache[key] = Node(value, expire)
        return true
    }
    
    override fun<T> add(key: String, value: T, expire: LocalDateTime): Boolean {
        add(key, value, Duration.between(LocalDateTime.now(), expire).seconds)
        return false
    }
    
    override fun<T> upd(key: String, value: T) { cache[key] = Node(value) }
    override fun<T> upd(key: String, value: T, expire: Long) { cache[key] = Node(value, expire) }
    override fun<T> upd(key: String, value: T, expire: LocalDateTime) { cache[key] = Node(value, Duration.between(LocalDateTime.now(), expire).seconds) }

    override fun <T> get(key: String, type: Class<T>): T? = cache[key]?.convert(type)
    override fun<T> pop(key: String, type: Class<T>): T? = cache.remove(key)?.convert(type)

    override fun has(key: String): Boolean {
        val result = cache[key]?.let { it.expire > timestamp() } ?: return false
        if(result) return true
        del(key)
        return false
    }

    override fun del(key: String) = cache.remove(key) != null
}