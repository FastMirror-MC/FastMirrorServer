package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.dto.Download
import com.github.fastmirrorserver.exception.Gone
import com.github.fastmirrorserver.exception.NotFound
import com.github.fastmirrorserver.service.DownloadService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.FileNotFoundException
import java.util.concurrent.TimeoutException
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/download")
class DownloadController {
    private val log = LoggerFactory.getLogger(DownloadController::class.java)
    @Autowired
    lateinit var service: DownloadService
    @GetMapping("/link")
    fun link(param: Download.Param): Download.Response {
        return service.query(param)
    }

    @GetMapping("/jar")
    fun download(@RequestParam("token") token: String, response: HttpServletResponse) {
        val url = "/download/jar?token=$token"
        try {
            service.download(token, response.outputStream)
        } catch (e: NullPointerException) {
            log.trace("invalid url $url", e)
            throw NotFound(
                errcode = 101,
                message = "resource not found",
                url = url
            )
        } catch (e: FileNotFoundException) {
            log.trace("invalid url $url", e)
            throw NotFound(
                errcode = 102,
                message = "resource not found",
                url = url
            )
        } catch (e: TimeoutException) {
            log.trace("try access expired resource: $url")
            throw Gone(
                errcode = 103,
                message = e.message ?: "",
                url = url
            )
        }
    }
}
