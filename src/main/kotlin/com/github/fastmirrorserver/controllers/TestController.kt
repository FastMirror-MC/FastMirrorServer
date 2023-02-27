package com.github.fastmirrorserver.controllers

import com.github.fastmirrorserver.annotations.Authority
import com.github.fastmirrorserver.utils.enums.Permission
import com.github.fastmirrorserver.utils.signature
import org.slf4j.LoggerFactory
import org.springframework.core.io.ByteArrayResource
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.support.HttpRequestHandlerServlet
import javax.servlet.http.HttpServletRequest

@RestController
class TestController {
    private val logger = LoggerFactory.getLogger(this::class.java)
    companion object {
        const val DEV_NULL = "/test/dev/null"
        const val PERMISSION_TEST = "/test/permission"
        const val UPLOAD_TEST = "/test/upload"
    }
    @RequestMapping(DEV_NULL, )
    @Authority(Permission.TESTER)
    fun `dev null`() {}
    
    @RequestMapping(PERMISSION_TEST)
    @Authority(Permission.ROOT, ignore_in_debug = false)
    fun `permission test`() {}
    
    @PutMapping(UPLOAD_TEST)
    fun `bytes upload test`(request: HttpServletRequest): String {
        val data = request.inputStream.readAllBytes() ?: ByteArray(0)
        logger.info("Range: ${request.getHeader("Range") ?: "null"}")
        logger.info("Content-Length: ${request.getHeader("Content-Length") ?: "null"}")
        logger.info("body_size = ${data.size}")
        logger.info("sha1 = ${data.signature()}")
        return data.signature()
    }
}
