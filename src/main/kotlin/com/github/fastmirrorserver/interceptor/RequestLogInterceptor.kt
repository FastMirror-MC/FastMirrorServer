package com.github.fastmirrorserver.interceptor

import com.github.fastmirrorserver.timestamp
import org.slf4j.LoggerFactory
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestLogInterceptor : HandlerInterceptor {
    private val log = LoggerFactory.getLogger(RequestLogInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (!super.preHandle(request, response, handler)) return false
        val addr = request.getHeader("x-real-ip") ?: request.getHeader("remote-host") ?: request.remoteAddr
        log.info("$addr send a ${request.method} request to ${request.requestURI}")
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        @Nullable ex: Exception?
    ) {
        val addr = request.getHeader("x-real-ip") ?: request.getHeader("remote-host") ?: request.remoteAddr

        log.info("Address: $addr")
        log.info("Timestamp: ${timestamp()}")
        log.info("Status: ${response.status}")
        log.info("User-Agent: ${request.getHeader("user-agent")}")
        log.info("X-Ratelimit-Session: ${request.getHeader("x-ratelimit-session")}")
        log.info("X-Ratelimit-Limit: ${request.getHeader("x-ratelimit-limit")}")
        log.info("X-Ratelimit-Remaining: ${request.getHeader("x-ratelimit-remaining")}")
        log.info("X-Ratelimit-Reset: ${request.getHeader("x-ratelimit-reset")}")
    }
}