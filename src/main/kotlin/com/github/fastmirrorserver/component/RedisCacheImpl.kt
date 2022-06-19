package com.github.fastmirrorserver.component

import com.github.fastmirrorserver.Json
import com.github.fastmirrorserver.timestamp
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisCacheImpl : Cache {
    @Autowired
    private lateinit var redis: StringRedisTemplate

    private val logger = LoggerFactory.getLogger(RedisCacheImpl::class.java)
    private final val prefix = "fms"
    private final val timeUnit = TimeUnit.SECONDS

    private fun warp(key: String): String = "${prefix}:${key}"

    override fun <T> add(key: String, value: T): Boolean {
        try {
            if(value == null) return false
            redis.opsForValue().set(warp(key), Json.serialization(value))
            return true
        } catch (e: Exception) {
            logger.error("exception occurred when add value into redis.", e)
            return false
        }
    }

    override fun <T> add(key: String, value: T, ttl: Int): Boolean {
        try {
            if(value == null) return false
            redis.opsForValue().set(warp(key), Json.serialization(value), ttl.toLong(), timeUnit)
            return true
        } catch (e: Exception) {
            logger.error("exception occurred when add value into redis.", e)
            return false
        }
    }

    override fun <T> add(key: String, value: T, expire: Long): Boolean
    = add(key, value, (expire - timestamp()).toInt())

    override fun <T> upd(key: String, value: T): Boolean {
        del(key)
        return add(key, value)
    }

    override fun <T> upd(key: String, value: T, ttl: Int): Boolean {
        del(key)
        return add(key, value, ttl)
    }

    override fun <T> upd(key: String, value: T, expire: Long): Boolean {
        del(key)
        return add(key, value, expire)
    }

    override fun <T> get(key: String, type: Class<T>): T?
    = get(key)?.let { Json.deserialization(it, type) }

    override fun get(key: String): String? = redis.opsForValue()[warp(key)].ifEmpty { null }

    override fun has(key: String): Boolean= redis.hasKey(key)

    override fun del(key: String): Boolean = redis.delete(warp(key))
}