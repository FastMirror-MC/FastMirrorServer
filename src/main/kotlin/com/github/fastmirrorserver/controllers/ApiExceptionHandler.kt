package com.github.fastmirrorserver.controllers

import com.github.fastmirrorserver.dtos.ApiResponse
import com.github.fastmirrorserver.exception.ApiException
import com.github.fastmirrorserver.utils.UTC
import com.github.fastmirrorserver.utils.uuid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.File
import java.io.PrintWriter
import java.lang.Exception
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class ApiExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @ExceptionHandler(ApiException::class)
    fun serviceExceptionHandler(e: ApiException, request: HttpServletRequest, response: HttpServletResponse): ApiResponse {
        response.status = e.status.value()
        return e.toResponse()
    }
    
    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateExceptionHandler(e: IllegalStateException, request: HttpServletRequest, response: HttpServletResponse): ApiResponse {
        return if(e.cause is ApiException) serviceExceptionHandler(e.cause as ApiException, request, response)
        else exceptionHandler(e, request, response)
    }
    
    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception, request: HttpServletRequest, response: HttpServletResponse): ApiResponse {
        response.status = 500
        val id = uuid().lowercase()
        val file = File("./error-reports/${id}")
        file.createNewFile()
        if(file.exists()) file.bufferedWriter().use {
            it.write("[${LocalDateTime.now().UTC}] ${e::class.java.canonicalName}: ${e.message}")
            it.newLine()
            val writer = PrintWriter(it)
            e.printStackTrace(writer)
            writer.flush()
        }
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