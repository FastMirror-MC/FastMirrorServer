package com.github.fastmirrorserver.entity

data class RequestLimit(
    val address: String?,
    val token: String,
    val expire: Long
) {
    companion object {
        const val MAX_TTL = 60
        const val MAX_REQ = 60
    }

    var remainRequestCount: Int = MAX_REQ
        private set(value) {
            field = if (value < 0) 0 else value
        }
    val canRequest: Boolean
        get() {
            return remainRequestCount-- > 0
        }
}
