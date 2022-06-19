package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.utc
import java.time.LocalDateTime

data class ApiError(
    val status: Int,
    val errcode: String,
    val message: String,
    val details: String,
    var url: String,
    private val timestamp: String = utc(LocalDateTime.now())
) {
    override fun toString(): String {
        return """
            {
                "timestamp": $timestamp,
                "status": $status,
                "errcode": $errcode
                "message": "$message",
                "details": "$details",
                "url": "$url"
            }
        """.trimIndent()
    }
}