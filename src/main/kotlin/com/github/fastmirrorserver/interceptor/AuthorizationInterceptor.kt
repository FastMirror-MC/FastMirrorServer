package com.github.fastmirrorserver.interceptor

import com.github.fastmirrorserver.annotation.Authority
import com.github.fastmirrorserver.component.Cache
import com.github.fastmirrorserver.exception.ApiException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.github.fastmirrorserver.service.AuthorizationService
import com.github.fastmirrorserver.util.enums.Permission
import com.github.fastmirrorserver.util.UTC
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthorizationInterceptor: HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(AuthorizationInterceptor::class.java)
    @Value("\${spring.profiles.active}")
    private lateinit var env: String
    @Autowired
    private lateinit var authorization: AuthorizationService
    @Autowired
    private lateinit var cache: Cache
    
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authority = if(handler !is HandlerMethod) Authority(Permission.NONE)
        else handler.method.getAnnotation(Authority::class.java) ?: Authority(Permission.NONE)
        val permission = authority.permission
        
        val traceback = authorization.verification(request, response)

        if(!request.requestURI.startsWith("/api/v3/upload/session/file"))
            logger.info("request detected. session=$traceback, method=${request.method}, path=${request.requestURI}")
        
        if(!(env == "debug" && traceback.permission == Permission.TESTER && authority.ignore_in_debug)) {
            if (permission == Permission.TESTER && traceback.permission != Permission.TESTER)
                throw ApiException.PERMISSION_DENIED
            if (permission.level > traceback.permission.level)
                throw ApiException.PERMISSION_DENIED
        }
        
        val requestable = traceback.requestable()
        
        if(!traceback.permission.unlimited_request) {
            response.setIntHeader("x-ratelimit-limit", traceback.permission.request_limit)
            response.setIntHeader("x-ratelimit-remaining", traceback.remain_request_count)
            response.setHeader("x-ratelimit-reset", traceback.next_refresh_time.UTC)
        }
        
        cache.upd(traceback.token, traceback, traceback.lifetime)
        cache.upd(traceback.user, traceback.token, traceback.lifetime)
        cache.upd(traceback.remote_address, traceback.token, traceback.lifetime)
        
        if(requestable) return true
        
        throw ApiException.REQUEST_LIMIT
    }
}