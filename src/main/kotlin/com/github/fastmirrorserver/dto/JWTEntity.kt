package com.github.fastmirrorserver.dto

data class JWTEntity (
    val access: String,
    val refresh: String
) {
    override fun toString(): String {
        return "{\n" +
                "  \"access\": \"$access\"\n" +
                "  \"refresh\": \"$refresh\"\n" +
                "}"
    }
}