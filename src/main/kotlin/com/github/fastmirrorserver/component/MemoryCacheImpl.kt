package com.github.fastmirrorserver.component

import com.github.fastmirrorserver.Json
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

    private val pool = HashMap<String, U>()

    private fun adds(key: String, value: String, expire: Long)
    = pool[key]?.let{ false } ?: let {
        pool[key] = U(value, expire)
        true
    }

    override fun <T> add(key: String, value: T, expire: Long): Boolean
    = if(value!!::class.java == String::class.java)
        adds(key, String::class.java.cast(value), expire)
    else
        adds(key, Json.serialization(value), expire)

    override fun <T> add(key: String, value: T, ttl: Int): Boolean
    = add(key, value, ttl + timestamp())

    override fun <T> add(key: String, value: T): Boolean
    = add(key, value, -1L)

    private fun updates(key: String, value: String, expire: Long): Boolean {
        if(adds(key, value, expire)) return true
        del(key)
        return adds(key, value, expire)
    }

    override fun <T> upd(key: String, value: T, expire: Long): Boolean
    = if(value!!::class.java == String::class.java)
        updates(key, String::class.java.cast(value), expire)
    else
        updates(key, Json.serialization(value), expire)

    override fun <T> upd(key: String, value: T, ttl: Int): Boolean
    = upd(key, value, ttl + timestamp())

    override fun <T> upd(key: String, value: T): Boolean
    = upd(key, value, -1L)

    override fun <T> get(key: String, type: Class<T>): T?
     = get(key)?.let { Json.deserialization(it, type) }

    override fun get(key: String)
    = pool[key]?.let {
        if(it.available)
            it.value.ifEmpty { null }
        else {
            pool.remove(key)
            null
        }
    }

    override fun has(key: String): Boolean = pool[key]?.let{
        if(!it.available){
            pool.remove(key)
            false
        } else true
    } ?: false

    override fun del(key: String)
     = if(pool.containsKey(key)) {
         pool.remove(key)
         true
    } else
        false
}
