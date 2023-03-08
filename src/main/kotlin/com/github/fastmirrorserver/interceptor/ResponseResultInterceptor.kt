package com.github.fastmirrorserver.interceptor

import com.github.fastmirrorserver.annotation.RawResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ResponseResultInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(handler !is HandlerMethod) return true

        if(handler.beanType.isAnnotationPresent(RawResponse::class.java))
            request.setAttribute("DISABLE_RESPONSE_WRAPPER", RawResponse::class.java)
        else if(handler.method.isAnnotationPresent(RawResponse::class.java))
            request.setAttribute("DISABLE_RESPONSE_WRAPPER", RawResponse::class.java)
        return true
    }
}