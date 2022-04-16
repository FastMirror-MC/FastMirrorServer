package com.github.fastmirrorserver.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fastmirrorserver.timestamp
import org.springframework.stereotype.Component

@Component
class MemoryCacheImpl : Cache {
    private data class U(
        val value: String,
        val expire: Long
    ) {
        val available: Boolean
            get() = when(expire) {
                -1L -> true
                else -> expire > timestamp()
            }
    }

    private val mapper = ObjectMapper()
    private val pool = HashMap<String, U>()

    private fun adds(key: String, value: String, expire: Long)
            = pool[key]?.let{ false } ?: let {
        pool[key] = U(value, expire)
        true
    }


    override fun <T> add(key: String, value: T): Boolean
            = add(key, value, -1)

    override fun <T> add(key: String, value: T, ttl: Int): Boolean
            = add(key, value, ttl + timestamp())

    override fun <T> add(key: String, value: T, expire: Long): Boolean {
        return if(value!!::class.java == String::class.java)
            adds(key, String::class.java.cast(value), expire)
        else
            adds(key, mapper.writeValueAsString(value), expire)
    }

    override fun <T> get(key: String, type: Class<T>): T?
     = get(key)?.let { mapper.readValue(it, type) }

    override fun get(key: String)
            = pool[key]?.let {
        if(it.available)
            it.value
        else {
            pool.remove(key)
            null
        }
    }

    override fun del(key: String)
     = if(pool.containsKey(key)) {
         pool.remove(key)
         true
    } else
        false
}
