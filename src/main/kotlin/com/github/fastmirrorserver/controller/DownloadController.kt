package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.dto.Download
import com.github.fastmirrorserver.exception.Gone
import com.github.fastmirrorserver.exception.NotFound
import com.github.fastmirrorserver.service.DownloadService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.FileNotFoundException
import java.util.concurrent.TimeoutException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Api("下载相关", description = "用于下载文件和获取文件下载链接", tags = ["download"])
@RestController
@RequestMapping("/download")
class DownloadController {
    private val log = LoggerFactory.getLogger(DownloadController::class.java)
    @Autowired
    lateinit var service: DownloadService

    @ApiOperation("获取下载链接")
    @GetMapping("/{name}/{version}/{build}")
    fun link(request: HttpServletRequest,
             @PathVariable name: String, @PathVariable version: String, @PathVariable build: String
    ): Download.Response {
        val param = Download.Param(name, version, build)
        try {
            return service.query(param)
        }catch (e: NullPointerException) {
            log.trace("${request.requestURI} not found", e)
            throw NotFound(
                errcode = 101,
                message = "resource not found",
                url = request.requestURI
            )
        }
    }

    @ApiOperation("下载文件")
    @GetMapping("/artifact")
    fun download(request: HttpServletRequest, response: HttpServletResponse,
                 @RequestParam("token") token: String) {
        val uri = request.requestURI
        try {
            service.download(token, response.outputStream)
        } catch (e: NullPointerException) {
            log.trace("invalid uri $uri", e)
            throw NotFound(
                errcode = 111,
                message = "resource not found",
                url = uri
            )
        } catch (e: FileNotFoundException) {
            log.trace("invalid uri $uri", e)
            throw NotFound(
                errcode = 112,
                message = "resource not found",
                url = uri
            )
        } catch (e: TimeoutException) {
            log.trace("try access expired resource: $uri")
            throw Gone(
                errcode = 113,
                message = e.message ?: "",
                url = uri
            )
        }
    }
}
