package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.entity.RequestLimit
import com.github.fastmirrorserver.uuid
import org.springframework.stereotype.Service

@Service
class RequestLimitService {
    private val pool = HashMap<String, RequestLimit>()

    private fun timestamp() = System.currentTimeMillis() / 1000

    private fun add(address: String, token: String): RequestLimit {
        val expire = timestamp() + RequestLimit.MAX_TTL
        val unit = RequestLimit(
            address = address,
            token = token,
            expire = expire
        )
        pool[address] = unit
        pool[token] = unit
        return unit
    }

    fun clear() {
        val timestamp = timestamp()
        for ((k, v) in pool) if (v.expire > timestamp) pool.remove(k)
    }

    fun get(address: String, token: String?): RequestLimit {
        val token2 = token ?: uuid()
        var unit = pool[address] ?: pool[token] ?: add(address, token2)
        if (unit.expire < timestamp()) unit = add(address, token2)
        return unit
    }
}
