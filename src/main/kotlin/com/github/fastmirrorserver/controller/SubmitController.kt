package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.dto.Submit
import com.github.fastmirrorserver.exception.BadRequest
import com.github.fastmirrorserver.exception.Forbidden
import com.github.fastmirrorserver.service.SubmitService
import org.postgresql.util.PSQLException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

@RestController
class SubmitController {
    @Autowired
    lateinit var submitService: SubmitService
    @PostMapping("/submit")
    fun submit(param: Submit, file: MultipartFile, request: HttpServletRequest) {
        try{
            param.token = request.getHeader("token")
            param.clientId = request.getHeader("clientId")
        } catch (e: NullPointerException) {
            e.printStackTrace()
            throw BadRequest(
                errcode = 401,
                message = "need more params"
            )
        }

        try {
            submitService.query(param).writeToLocal(file.inputStream)
        } catch (e: PSQLException) {
            e.printStackTrace()
            throw Forbidden(
                errcode = 402,
                message = "the server already has the same resource",
                details = e.message ?: "no further information"
            )
        }
    }
}