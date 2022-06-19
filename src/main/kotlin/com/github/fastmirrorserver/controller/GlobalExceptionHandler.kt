package com.github.fastmirrorserver.controller

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.github.fastmirrorserver.ErrorCodes
import com.github.fastmirrorserver.dto.ApiError
import com.github.fastmirrorserver.exception.ServiceException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException::class)
    fun serviceExceptionHandler(e: ServiceException, request: HttpServletRequest, response: HttpServletResponse): ApiError {
        response.status = e.entity.status
        e.entity.url = request.requestURI
        return e.entity
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun tokenExpiredHandler(request: HttpServletRequest, response: HttpServletResponse): ApiError {
        response.status = 401
        return ApiError(
            status = 401,
            errcode = ErrorCodes.Auth.token_timeout,
            url = request.requestURI,
            message = "The token has expired. Please try refresh_token it or logging in again.",
            details = "no further information"
        )
    }

    @ExceptionHandler(JWTVerificationException::class)
    fun jwtVerificationHandler(request: HttpServletRequest, response: HttpServletResponse): ApiError {
        response.status = 401
        return ApiError(
            status = 401,
            errcode = ErrorCodes.Auth.token_tampered,
            url = request.requestURI,
            message = "Token verification failed.",
            details = "no further information"
        )
    }
}
