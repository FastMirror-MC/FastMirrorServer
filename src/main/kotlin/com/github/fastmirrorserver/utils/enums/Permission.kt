package com.github.fastmirrorserver.utils.enums

enum class Permission(
    val level: Int, 
    val request_limit: Int = 0, 
    val refresh_period: Int = 0, 
    val unlimited_request: Boolean = false
) {
    TESTER(0, 3, 10),
    NONE(1, 200, 60 * 60),
    USER(2, 500, 60 * 60),
    COLLECTOR(3, unlimited_request = true),
    ROOT(4, unlimited_request = true),
}