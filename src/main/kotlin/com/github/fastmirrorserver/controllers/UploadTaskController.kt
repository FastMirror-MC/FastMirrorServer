package com.github.fastmirrorserver.controllers

import com.github.fastmirrorserver.dtos.Manifest
import com.github.fastmirrorserver.services.UploadTaskService
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
    fun createTask(@RequestBody manifest: Manifest)
     = service.createTask(manifest)
    
    @PutMapping(UPLOAD)
    fun upload(
        @PathVariable name: String, 
        @PathVariable mc_version: String, 
        @PathVariable core_version: String, 
        request: HttpServletRequest
    ) = service.uploadFile(name, mc_version, core_version, request)

    @PutMapping(CLOSE_TASK)
    fun closeTask(
        @PathVariable name: String,
        @PathVariable mc_version: String,
        @PathVariable core_version: String
    ) = service.closeTask(name, mc_version, core_version)
}
