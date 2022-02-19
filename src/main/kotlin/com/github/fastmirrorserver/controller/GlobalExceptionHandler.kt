package com.github.fastmirrorserver.controller

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
        log.trace("catch a uncaught exception.", e)
        return e.entity
    }
}
