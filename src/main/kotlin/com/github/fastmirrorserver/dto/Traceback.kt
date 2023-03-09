package com.github.fastmirrorserver.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.fastmirrorserver.util.enums.Permission
import com.github.fastmirrorserver.util.uuid
import java.time.LocalDateTime
import javax.servlet.http.Cookie

data class Traceback(
    val user: String,
    val remote_address: String,
    val permission: Permission,
    val token: String = uuid(),
    private var _remain_request_count: Int = permission.request_limit,
    private var _next_refresh_time: LocalDateTime = LocalDateTime.now().plusSeconds(permission.refresh_period.toLong()),
    val lifetime: LocalDateTime = LocalDateTime.now().plusHours(2)
) {
    companion object {
        const val COOKIE_NAME = "traceback"
    }
    @get:JsonIgnore
    val remain_request_count get() = if(_remain_request_count < 0) 0 else _remain_request_count
    @get:JsonIgnore
    val next_refresh_time get() = _next_refresh_time
    
    fun requestable(): Boolean {
        if(permission.unlimited_request) return true
        if(_next_refresh_time < LocalDateTime.now()) {
            _remain_request_count = permission.request_limit
            _next_refresh_time = LocalDateTime.now().plusSeconds(permission.refresh_period.toLong())
        }
        
        // 你能一分钟访问2,147,483,648次我把键盘吃下去
        // return if(_remain_request_count == 0) false else _remain_request_count --> 0
        return _remain_request_count --> 0
    }
    
    fun toCookie(): Cookie {
        val cookie = Cookie(COOKIE_NAME, token)
        cookie.path = "/"
        cookie.maxAge = -1
        cookie.secure = true
        return cookie
    }

    override fun toString(): String
    = "Session{user=$user, token=$token, ip=$remote_address, remain_request=$remain_request_count, next_refresh_time=$next_refresh_time}"
}
