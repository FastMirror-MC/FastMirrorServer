package com.github.fastmirrorserver.dto

data class JWTEntity (
    val access_token: String,
    val refresh_token: String,
    val expires_at: Long,
    val scope: String = "all"
) {
    override fun toString(): String {
        return "{\n" +
                "  \"access_token\": \"$access_token\"\n" +
                "  \"refresh_token\": \"$refresh_token\"\n" +
                "}"
    }
}