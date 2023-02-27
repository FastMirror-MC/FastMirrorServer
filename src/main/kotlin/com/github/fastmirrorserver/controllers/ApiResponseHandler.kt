package com.github.fastmirrorserver.controllers

import com.github.fastmirrorserver.annotations.RawResponse
import com.github.fastmirrorserver.dtos.ApiResponse
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import java.lang.Exception

@ControllerAdvice
class ApiResponseHandler : ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        return attributes.request.getAttribute("DISABLE_RESPONSE_WRAPPER") == null
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val no_content = returnType.getMethodAnnotation(ResponseStatus::class.java)?.let { 
            it.code == HttpStatus.NO_CONTENT || it.value == HttpStatus.NO_CONTENT 
        } ?: false
        val raw_response = returnType.getMethodAnnotation(RawResponse::class.java) != null

        if(no_content || raw_response || body is String || body is ApiResponse) return body
        if(body is Exception) return ApiResponse.failed(body)
        return ApiResponse.success(body)
    }
}