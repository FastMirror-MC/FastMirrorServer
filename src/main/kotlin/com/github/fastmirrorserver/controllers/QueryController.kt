package com.github.fastmirrorserver.controllers

import com.github.fastmirrorserver.annotations.RawResponse
import com.github.fastmirrorserver.entitys.toResponse
import com.github.fastmirrorserver.services.FileService
import com.github.fastmirrorserver.utils.*
import org.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class QueryController {
    companion object {
        const val QUERY_ALL_PROJECT = "/api/v3"
        const val QUERY_SUPPORTED_MC_VER_OF_PROJECT = "/api/v3/{name}"
        const val QUERY_ALL_CORE_VER_OF_MC_VER = "/api/v3/{name}/{mc_version}"
        const val QUERY_SPECIFIC_ARTIFACT = "/api/v3/{name}/{mc_version}/{core_version}"
        const val DOWNLOAD = "/download/{name}/{mc_version}/{core_version}"
    }
    
    @Autowired
    private lateinit var database: Database
    @Autowired
    private lateinit var file_service: FileService
    
    @GetMapping(QUERY_ALL_PROJECT)
    fun queryAllProject(@RequestParam("project", required = false) projects: ArrayList<String>?)
            = projects?.map { database.getSupportedMcVersionOfProject(it) } ?: database.getAllProject()

    @GetMapping(QUERY_SUPPORTED_MC_VER_OF_PROJECT)
    fun querySupportedMcVersionOfProject(
        @PathVariable name: String
        ) = database.getSupportedMcVersionOfProject(name)

    @GetMapping(QUERY_ALL_CORE_VER_OF_MC_VER)
    fun queryAllCoreVersionOfMcVersion(
        @PathVariable name: String,
        @PathVariable mc_version: String,
        @RequestParam("offset", required = false) offset: Int?,
        @RequestParam("limit", required = false) limit: Int?
        ) = database.getAllCoreVersionOfMcVersion(name, mc_version, offset, limit)

    @GetMapping(QUERY_SPECIFIC_ARTIFACT)
    fun querySpecificArtifact(
        @PathVariable name: String,
        @PathVariable core_version: String, 
        @PathVariable mc_version: String
        ) = database.getSpecificArtifact(name, mc_version, core_version)
        .toResponse()

    @GetMapping(DOWNLOAD)
    @RawResponse
    fun download(
        @PathVariable name: String,
        @PathVariable core_version: String,
        @PathVariable mc_version: String
        ) = file_service.send(name, core_version, mc_version)
}