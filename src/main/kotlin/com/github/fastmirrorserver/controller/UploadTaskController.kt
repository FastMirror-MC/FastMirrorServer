package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.annotation.Authority
import com.github.fastmirrorserver.pojo.ManifestPOJO
import com.github.fastmirrorserver.service.UploadTaskService
import com.github.fastmirrorserver.util.enums.Permission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class UploadTaskController {
    @Autowired
    private lateinit var service: UploadTaskService
    companion object {
        const val CREATE_TASK = "/api/v3/upload/session/create"
        const val UPLOAD = "/api/v3/upload/session/file/{name}/{mc_version}/{core_version}"
        const val CLOSE_TASK = "/api/v3/upload/session/close/{name}/{mc_version}/{core_version}"
    }
    
    @PostMapping(CREATE_TASK)
    @Authority(Permission.COLLECTOR)
    fun createTask(@RequestBody manifest: ManifestPOJO, request: HttpServletRequest)
     = service.createTask(manifest, request)
    
    @PutMapping(UPLOAD)
    @Authority(Permission.COLLECTOR)
    fun upload(
        @PathVariable name: String, 
        @PathVariable mc_version: String, 
        @PathVariable core_version: String, 
        request: HttpServletRequest
    ) = service.uploadFile(name, mc_version, core_version, request)

    @PutMapping(CLOSE_TASK)
    @Authority(Permission.COLLECTOR)
    fun closeTask(
        @PathVariable name: String,
        @PathVariable mc_version: String,
        @PathVariable core_version: String
    ) = service.closeTask(name, mc_version, core_version)
}
