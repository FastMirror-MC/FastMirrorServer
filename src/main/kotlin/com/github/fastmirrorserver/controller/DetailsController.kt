package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.ErrorCodes
import com.github.fastmirrorserver.exception.NotFound
import com.github.fastmirrorserver.service.DetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2")
class DetailsController {
    @Autowired
    lateinit var service: DetailsService

    @GetMapping("")
    fun summary() = service.summary()

    @GetMapping("/all")
    fun versions() = service.versions()

    @GetMapping("/{name}")
    fun versions(@PathVariable name: String) = service.versions(name)

    @GetMapping("/{name}/{version}")
    fun coreVersions(
        @PathVariable name: String, @PathVariable version: String,
        @RequestParam("offset", required = false) offset: Int?,
        @RequestParam("limit", required = false) limit: Int?
    ) = service.coreVersion(name, version, offset ?: 0, limit ?: 25)

    @GetMapping("/{name}/{version}/{coreVersion}")
    fun artifact(
        @PathVariable name: String, @PathVariable version: String, @PathVariable coreVersion: String
    ) = try {
        service.artifact(name, version, coreVersion)
    } catch (e: NullPointerException) {
        throw NotFound(
            errcode = ErrorCodes.Details.resource_not_found,
            message = "resource not found"
        )
    }

    @GetMapping("/{name}/{version}/{coreVersion}/download")
    fun download(
        @PathVariable name: String, @PathVariable version: String, @PathVariable coreVersion: String
    ) = service.download(name, version, coreVersion)
}
