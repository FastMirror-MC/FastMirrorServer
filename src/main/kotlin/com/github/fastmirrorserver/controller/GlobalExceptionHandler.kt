package com.github.fastmirrorserver.controller

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.github.fastmirrorserver.dto.ApiError
import com.github.fastmirrorserver.exception.ServiceException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(ServiceException::class)
    fun serviceExceptionHandler(e: ServiceException, request: HttpServletRequest, response: HttpServletResponse): ApiError {
        response.status = e.entity.status
        e.entity.url = request.requestURI
//        log.trace("catch a uncaught exception.", e)
        return e.entity
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun tokenExpiredHandler(request: HttpServletRequest, response: HttpServletResponse): ApiError {
        response.status = 401
        return ApiError(
            status = 401,
            errcode = 501,
            url = request.requestURI,
            message = "The token has expired. Please try refresh it or logging in again.",
            details = "no further information"
        )
    }

    @ExceptionHandler(JWTVerificationException::class)
    fun jwtVerificationHandler(request: HttpServletRequest, response: HttpServletResponse): ApiError {
        response.status = 401
        return ApiError(
            status = 401,
            errcode = 501,
            url = request.requestURI,
            message = "Token verification failed.",
            details = "no further information"
        )
    }
}
