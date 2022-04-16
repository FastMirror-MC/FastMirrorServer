package com.github.fastmirrorserver.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SubmitAuthorizationInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(!super.preHandle(request, response, handler)) return false
        val authorization = request.getHeader("Authorization")
        return true
    }
}
