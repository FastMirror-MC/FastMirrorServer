package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.dto.ApiError
import com.github.fastmirrorserver.exception.ServiceException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException::class)
    public fun serviceExceptionHandler(e: ServiceException, response: HttpServletResponse): ApiError {
        response.status = e.entity.status
        return e.entity
    }
}