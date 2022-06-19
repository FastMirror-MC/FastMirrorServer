package com.github.fastmirrorserver.interceptor

import com.github.fastmirrorserver.ErrorCodes
import com.github.fastmirrorserver.exception.ServiceException
import com.github.fastmirrorserver.exception.Unauthorized
import com.github.fastmirrorserver.getAuthorization
import com.github.fastmirrorserver.service.AuthorizationService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SubmitAuthorizationInterceptor : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(SubmitAuthorizationInterceptor::class.java)
    @Autowired
    private lateinit var service: AuthorizationService
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(!super.preHandle(request, response, handler)) return false
        try {
            val (method, body) = getAuthorization(request)
            if(method == "Basic") service.login(body)
            else if(method == "Bearer") service.verify(body)
            return true
        } catch (e: ServiceException) {
            logger.error("Authorize failed.", e)
            throw Unauthorized(message = e.entity.message, errcode = e.entity.errcode, details = e.entity.details)
        } catch (e: Exception) {
            logger.error("Authorize failed.", e)
            throw Unauthorized(message = e.message ?: e::class.java.name, errcode = ErrorCodes.Auth.general)
        }
    }
}
