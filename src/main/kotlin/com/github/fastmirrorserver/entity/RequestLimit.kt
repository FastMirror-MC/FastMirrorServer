package com.github.fastmirrorserver.entity

import com.github.fastmirrorserver.timestamp

data class RequestLimit(
    val address: String,
    val token: String,
    val ttl: Int = MAX_TTL,
    private val createTime: Long = timestamp()
) {
    companion object {
        const val MAX_TTL = 60
        const val MAX_REQ = 60
    }

    private val lazyExpire = lazy { createTime + ttl }

    val expire: Long
        get() = lazyExpire.value

    var remainRequestCount: Int = MAX_REQ
        private set(value) { field = if (value < 0) 0 else value }

    val canRequest: Boolean
        get() = remainRequestCount --> 0
}
