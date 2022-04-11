package com.github.fastmirrorserver.interceptor

import com.github.fastmirrorserver.entity.RequestLimit
import com.github.fastmirrorserver.exception.Forbidden
import com.github.fastmirrorserver.service.RequestLimitService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestContextInterceptor : HandlerInterceptor {
    private val log: Logger = LoggerFactory.getLogger(RequestContextInterceptor::class.java)
    @Autowired
    private lateinit var service: RequestLimitService
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (!super.preHandle(request, response, handler)) return false
        val remoteAddr = request.getHeader("x-real-ip") ?: request.getHeader("remote-host") ?: request.remoteAddr
        log.info("$remoteAddr send a ${request.method} request to ${request.requestURI}")
        log.info("User-Agent: ${request.getHeader("user-agent")}")

        val limit = service.get(remoteAddr, request.getHeader("x-ratelimit-session"))
        response.setHeader("x-ratelimit-session", limit.token)
        response.setIntHeader("x-ratelimit-limit", RequestLimit.MAX_REQ)
        response.setIntHeader("x-ratelimit-remaining", limit.remainRequestCount)
        response.setHeader("x-ratelimit-reset", limit.expire.toString())
        if (limit.canRequest) return true
        throw Forbidden(
            errcode = 1001,
            message = "API rate limit exceeded for $remoteAddr",
            details = "Please reduce the frequency of access"
        )
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        super.afterCompletion(request, response, handler, ex)
    }
}
