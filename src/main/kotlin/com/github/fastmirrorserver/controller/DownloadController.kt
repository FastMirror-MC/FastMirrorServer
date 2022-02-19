package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.exception.Gone
import com.github.fastmirrorserver.exception.NotFound
import com.github.fastmirrorserver.service.DownloadService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.FileNotFoundException
import java.util.concurrent.TimeoutException
import javax.servlet.http.HttpServletResponse

@Api("下载相关", description = "用于下载文件和获取文件下载链接", tags = ["download"])
@RestController
@RequestMapping("/download")
class DownloadController {
    @Autowired
    lateinit var service: DownloadService

    @GetMapping("")
    fun download(response: HttpServletResponse,
                 @RequestParam("token") token: String) {
        try {
            response.addHeader("Content-Disposition", "attachment;filename=${service.getFilename(token)}")
            service.download(token, response.outputStream)
        } catch (e: NullPointerException) {
            throw NotFound(
                errcode = 111,
                message = "resource not found"
            )
        } catch (e: FileNotFoundException) {
            throw NotFound(
                errcode = 112,
                message = "resource not found"
            )
        } catch (e: TimeoutException) {
            throw Gone(
                errcode = 113,
                message = e.message ?: ""
            )
        }
    }
}
