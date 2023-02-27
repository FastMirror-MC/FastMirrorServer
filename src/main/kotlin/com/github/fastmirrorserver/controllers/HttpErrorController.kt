package com.github.fastmirrorserver.controllers

import com.github.fastmirrorserver.dtos.ApiResponse
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class HttpErrorController : ErrorController {
    @RequestMapping("/error")
    fun handler(request: HttpServletRequest): ApiResponse {
        val code = request.getAttribute("javax.servlet.error.status_code") as Int
        val status = HttpStatus.valueOf(code)
        return ApiResponse(
            data = null,
            code = "err::status_code::${status.series().name.lowercase()}",
            success = false,
            message = status.reasonPhrase
        )
    }
}