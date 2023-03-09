package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.annotation.Authority
import com.github.fastmirrorserver.service.ErrorReportService
import com.github.fastmirrorserver.util.enums.Permission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class TracebackController {
    @Autowired
    private lateinit var service: ErrorReportService
    
    @GetMapping("/monitor/error-report/{id}")
    @Authority(Permission.ROOT)
    fun display(@PathVariable id: String): String = service.get(id)
}