package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.ErrorCodes
import com.github.fastmirrorserver.exception.Gone
import com.github.fastmirrorserver.exception.NotFound
import com.github.fastmirrorserver.service.DownloadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.FileNotFoundException
import java.util.concurrent.TimeoutException
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/download")
class DownloadController {
    @Autowired
    lateinit var service: DownloadService

    @GetMapping("")
    fun download(response: HttpServletResponse, @RequestParam("token") token: String) {
        try {
            val (info, buffer) = service.readFile(token)
            response.addHeader("Content-Disposition", "attachment;filename=${info.filename}")
            response.addHeader("Content-Length", buffer.size.toString())
            response.outputStream.write(buffer)
        } catch (e: FileNotFoundException) {
            throw NotFound(
                errcode = ErrorCodes.Download.file_not_found,
                message = "resource not found."
            )
        }  catch (e: IllegalArgumentException) {
            throw NotFound(
                errcode = ErrorCodes.core_info_not_found,
                message = "resource not found."
            )
        } catch (e: TimeoutException) {
            throw Gone(
                errcode = ErrorCodes.Download.timeout,
                message = "no further information."
            )
        }
    }
}
