﻿package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.dto.ApiResponse
import com.github.fastmirrorserver.exception.ApiException
import com.github.fastmirrorserver.service.ErrorReportService
import com.github.fastmirrorserver.util.UTC
import com.github.fastmirrorserver.util.uuid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.util.NestedServletException
import java.io.File
import java.io.PrintWriter
import java.lang.Exception
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class ApiExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @Autowired
    private lateinit var service: ErrorReportService
    
    @ExceptionHandler(ApiException::class)
    fun serviceExceptionHandler(e: ApiException, request: HttpServletRequest, response: HttpServletResponse): ApiResponse {
        response.status = e.status.value()
        return e.toResponse()
    }
    
    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception, request: HttpServletRequest, response: HttpServletResponse): ApiResponse {
        if(e.cause != null && e.cause is ApiException) return serviceExceptionHandler(e.cause as ApiException, request, response)
        logger.error("uncaught exception: ", e)
        response.status = 500
        
        val id = service.set(e)
        
        return ApiResponse(
            data = mapOf<String, String>(
                "name" to e::class.java.name,
                "message" to (e.message ?: "none"),
                "x-ray-id" to id
            ),
            code = "err::internal_server_error",
            success = false,
            message = "Internal server error. Please send this message to website administrator."
        )
    }
}