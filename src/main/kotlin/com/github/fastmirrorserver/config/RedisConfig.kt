package com.github.fastmirrorserver.config

import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.nio.ByteBuffer

@Configuration
@EnableCaching
class RedisConfig : CachingConfigurerSupport() {
    @Bean
    fun stringIntTemplate(factory: RedisConnectionFactory): RedisTemplate<String, Int> {
        val template = RedisTemplate<String, Int>()
        template.connectionFactory = factory

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = object : RedisSerializer<Int> {
            override fun serialize(t: Int?): ByteArray? = t?.let {
                val tmp = ByteArray(4)
                tmp[0] = ((it shr 24) and 0xff).toByte()
                tmp[1] = ((it shr 16) and 0xff).toByte()
                tmp[2] = ((it shr 8) and 0xff).toByte()
                tmp[3] = (it and 0xff).toByte()
                tmp
            }

            override fun deserialize(bytes: ByteArray?): Int? = bytes?.let { ByteBuffer.wrap(bytes).int }
        }
        template.hashKeySerializer = template.keySerializer
        template.hashValueSerializer = template.valueSerializer
        template.afterPropertiesSet()

        return template
    }
}