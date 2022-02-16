package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.dto.History
import com.github.fastmirrorserver.dto.Latest
import com.github.fastmirrorserver.exception.Forbidden
import com.github.fastmirrorserver.service.HistoryService
import com.github.fastmirrorserver.service.LatestService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.naming.LimitExceededException
import javax.servlet.http.HttpServletRequest

@Api("服务端核心信息获取", description = "服务端核心信息获取")
@RestController
@RequestMapping("/api")
class ApiController {
    @Autowired
    lateinit var latestService: LatestService

    @ApiOperation("获取服务端最新版本")
    @GetMapping("/latest")
    fun latest(request: HttpServletRequest, param: Latest.Param): Map<String, Map<String, Latest.ResponseUnit>> {
        try{ return latestService.query(param) }
        catch (e: LimitExceededException) {
            throw Forbidden(
                errcode = 201,
                message = e.message ?: "no further information",
                url = request.requestURI
            )
        }
    }

    @Autowired
    lateinit var historyService: HistoryService

    @ApiOperation("获取服务端历史版本")
    @GetMapping("/history")
    fun history(request: HttpServletRequest, param: History.Param): Map<String, Map<String, List<History.Response>>> {
        try { return historyService.query(param) }
        catch (e: LimitExceededException) {
            throw Forbidden(
                errcode = 201,
                message = e.message ?: "no further information",
                url = request.requestURI
            )
        }
    }
}