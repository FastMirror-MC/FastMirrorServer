package com.github.fastmirrorserver.interceptor

import com.github.fastmirrorserver.ErrorCodes
import com.github.fastmirrorserver.entity.RequestLimit
import com.github.fastmirrorserver.exception.Forbidden
import com.github.fastmirrorserver.service.RequestLimitService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestLimitInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var service: RequestLimitService
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (!super.preHandle(request, response, handler)) return false
        val remoteAddr = request.getHeader("x-real-ip") ?: request.getHeader("remote-host") ?: request.remoteAddr

        val limit = service.get(remoteAddr, request.getHeader("x-ratelimit-session"))
        response.setHeader("x-ratelimit-session", limit.token)
        response.setIntHeader("x-ratelimit-limit", RequestLimit.MAX_REQ)
        response.setIntHeader("x-ratelimit-remaining", limit.remainRequestCount)
        response.setHeader("x-ratelimit-reset", limit.expire.toString())
        if (limit.canRequest) return true
        throw Forbidden(
            errcode = ErrorCodes.Limit.maximum,
            message = "API rate limit exceeded for $remoteAddr",
            details = "Please reduce the frequency of access_token"
        )
    }
}
